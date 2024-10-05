package com.maycdevs.jobsearch.controller.frontend;

import com.maycdevs.jobsearch.api.FilesApiController;
import com.maycdevs.jobsearch.model.*;
import com.maycdevs.jobsearch.service.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.bind.DefaultValue;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.method.annotation.MvcUriComponentsBuilder;

import java.nio.file.Path;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Slf4j
@Controller
@RequestMapping("/offer")
public class OfferFrontendController {

    @Autowired
    private OfferService offerService;
    @Autowired
    private ActivitySectorService activitySectorService;
    @Autowired
    private TypeOffreService typeOffreService;
    @Autowired
    private NiveauScolaireService niveauScolaireService;
    @Autowired
    private NiveauExperienceService niveauExperienceService;
    @Autowired
    private CompanyService companyService;
    @Autowired
    private UserService userService;
    @Autowired
    private RoleService roleService;
    @Autowired
    private FileStorageService fileStorageService;

    private String baseControllerUrl = "offer";
    private String templateFileUrl = "frontend/offer";

    @GetMapping({"","/","/list","/search"})
    public String listOffersPage(Model model, Authentication authentication) throws Exception {
        // on recupere les donnees de l'user qui est connecter
        User authenticatedUser = null;
        if(authentication != null && authentication.getName() != null) {
            authenticatedUser = userService.getOneByUsername(authentication.getName());
        }

        model.addAttribute("authenticatedUser",authenticatedUser);
        model.addAttribute("allActivitySector", activitySectorService.getAll());
        model.addAttribute("allOffers", offerService.getAll());

        return templateFileUrl+"/search";
    }

    @PostMapping("/search")
    public String listSearchedOffersByCriteria(
            Model model,
            Authentication authentication,
            @RequestParam("sector") String sector
    ) throws Exception {
        if(sector.equals("") || sector.isBlank() || sector.equals("null")) {
            return "redirect:/offer/search";
        }

        List<Offer> data = new ArrayList<>();
        offerService.getAll().forEach(offer -> {
            if (offer.getActivitySector().getLibelle().equals(sector)) {
                data.add(offer);
            }
        });

        model.addAttribute("allActivitySector", activitySectorService.getAll());
        model.addAttribute("allOffers", data);

        return templateFileUrl+"/search";
    }

    @GetMapping("/{id}")
    public String offerDetailsPage(
            Model model,
            Authentication authentication,
            @PathVariable("id") String id
    ) throws Exception {
        // on recupere les donnees de l'user qui est connecter
        User authenticatedUser = null;
        if(authentication != null && authentication.getName() != null) {
            authenticatedUser = userService.getOneByUsername(authentication.getName());
        }

        Offer data = offerService.getOneById(id);
        if(data != null) {
            model.addAttribute("offer", data);
            model.addAttribute("authenticatedUser",authenticatedUser);
            return templateFileUrl+"/show";
        }

        return listOffersPage(model, authentication);
    }

    @GetMapping("/save")
    public String createOffer(Model model, Authentication authentication) throws Exception {
        return saveOfferPage(model, authentication, null);
    }

    @GetMapping("/save/{id}")
    public String saveOfferPage(
            Model model,
            Authentication authentication,
            @PathVariable("id") String id
    ) throws Exception {
        // on recupere les donnees de l'user qui est connecter
        User authenticatedUser = null;
        if(authentication != null && authentication.getName() != null) {
            authenticatedUser = userService.getOneByUsername(authentication.getName());
            Role candidatRole = roleService.getOneByLibelle("candidat");
            if(
                    candidatRole != null && authenticatedUser.getRole().getId().equals(candidatRole.getId()) ||
                    authenticatedUser.getRole().getLibelle().equalsIgnoreCase("candidat")
            ) {
                return "redirect:/account/profile";
            }
        }

        Offer data = null;
        String actionText = "Ajouter";
        if(id != null) {
            actionText = "Modifier";
            // on recuprere l'offre dont l'identifiant est la veleur de id
            data = offerService.getOneById(id);

            if(data == null) {
                return createOffer(model, authentication);
            }
        }

        model.addAttribute("authenticatedUser",authenticatedUser);
        model.addAttribute("allActivitySector", activitySectorService.getAll());
        model.addAttribute("allTypeOffer", typeOffreService.getAll());
        model.addAttribute("allSchoolLevel", niveauScolaireService.getAll());
        model.addAttribute("allLevelExperience", niveauExperienceService.getAll());
        model.addAttribute("allCompany", companyService.getAll());
        model.addAttribute("allUsers", userService.getAll());
        model.addAttribute("actionText", actionText);
        model.addAttribute("offer", data);
        return templateFileUrl+"/save";
    }

    @PostMapping("/save")
    public ModelAndView submitSaveOfferForm(
            @ModelAttribute Offer data,
            @RequestParam("dateExpirationInString") String dateExpirationInString,
            @RequestParam("file") MultipartFile file
    ) throws Exception {
        // Gestion du fichier si la valeur de son parametre est != null
        if(file != null && !Objects.requireNonNull(file.getOriginalFilename()).isBlank() && !file.isEmpty()) {
            fileStorageService.store(file);
            Path uploadedFilePath = fileStorageService.load(file.getOriginalFilename());
            String fileLocation = MvcUriComponentsBuilder.fromMethodName(FilesApiController.class,
                    "displayFile", file.getOriginalFilename()).build().toUri().toString();
            data.setImageLocation(fileLocation);
        }


        // on converti la valeur de la variable dateExpirationInString en type Date
        Date dateExpiration = new SimpleDateFormat("yyyy-MM-dd").parse(dateExpirationInString);
        data.setDateExpiration(dateExpiration);
        data.setEtat("enabled".toUpperCase());
        data.setLocalisation("abidjan".toUpperCase());

        Offer result = null;
        if(data.getId() == null || data.getId().toString().isBlank() || data.getId().toString().equals("-1")) {
            // creation
            result = offerService.create(data);
        } else {
            // modification
            result = offerService.update(data.getId().toString(), data);
        }

        if(result != null) {
            return new ModelAndView("redirect:/offer/list");
        }

        return new ModelAndView("redirect:/offer/save");
    }
}
