package com.maycdevs.jobsearch.controller.backend;


import com.maycdevs.jobsearch.api.FilesApiController;
import com.maycdevs.jobsearch.model.ActivitySector;
import com.maycdevs.jobsearch.model.Offer;
import com.maycdevs.jobsearch.model.User;
import com.maycdevs.jobsearch.service.*;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.mapping.Any;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.core.Authentication;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.method.annotation.MvcUriComponentsBuilder;

import java.nio.file.Path;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Objects;

@Slf4j
@Controller
@RequestMapping("/backend/offer")
public class OfferController {

    @Autowired
    private OfferService offerService;
    @Autowired
    private UserService userService;
    @Autowired
    private ActivitySectorService activitySectorService;
    @Autowired
    private NiveauScolaireService niveauScolaireService;
    @Autowired
    private NiveauExperienceService niveauExperienceService;
    @Autowired
    private TypeOffreService typeOffreService;
    @Autowired
    private CompanyService companyService;
    @Autowired
    private AttachementService manageFilesService;
    @Autowired
    private FileStorageService fileStorageService;

    private String baseControllerUrl = "backend/offer";
    private String templateFileUrl = "backend/offer";

    /*
    public String isUserHasAccess(Authentication authentication) throws Exception {
        User authenticatedUser = null;
        String text="";

        if(authentication != null && !authentication.getName().isBlank()) {
            authenticatedUser = userService.getOneByUsername(authentication.getName());
            if(authenticatedUser != null) {
                if (authenticatedUser.getRole().getLibelle().equalsIgnoreCase("admin")) {
                    // si l'user connecter est un admin
                    text = "true";
                } else {
                    // si l'user connecter est un candidat ou un recruteur, alors il n'a pas acces a cette page
                    // on le deconnecte
                    text = "account/profile";
                }
            } else {
                // Aucun user connecter, on redirige vers la page de connexion
                text = "login";
            }
        } else {
            // Aucun user connecter, on redirige vers la page de connexion
            text = "login";
        }

        return text;
    }
    */

    @GetMapping({"","/","/list"})
    public String manageOffer(Model model, Authentication authentication) throws Exception {
        /*
        String result = isUserHasAccess(authentication);
        if(!Objects.equals(result, "true")) {
            return "redirect:/"+result;
        }
        */
        User authenticatedUser = userService.getOneByUsername(authentication.getName());
        model.addAttribute("authenticatedUser", authenticatedUser);
        model.addAttribute("allOffer", offerService.getAll());
        return templateFileUrl+"/list";
    }

    @GetMapping("/add")
    public String createOfferFromBackend(Model model, Authentication authentication) throws Exception {
        User authenticatedUser = userService.getOneByUsername(authentication.getName());
        model.addAttribute("authenticatedUser", authenticatedUser);

        model.addAttribute("sectors", activitySectorService.getAll());
        model.addAttribute("schoolLevels", niveauScolaireService.getAll());
        model.addAttribute("levelExperiences", niveauExperienceService.getAll());
        model.addAttribute("typeOffres", typeOffreService.getAll());
        model.addAttribute("companies", companyService.getAll());

        return templateFileUrl+"/save";
    }

    @GetMapping("/update/{id}")
    public String updateOfferFromBackend(
            Model model,
            Authentication authentication,
            @PathVariable("id") String id
    ) throws Exception {
        User authenticatedUser = userService.getOneByUsername(authentication.getName());
        model.addAttribute("authenticatedUser", authenticatedUser);

        Offer data = offerService.getOneById(id);
        if(data == null) {
            return "redirect:/backend/offer/list";
        }

        model.addAttribute("sectors", activitySectorService.getAll());
        model.addAttribute("schoolLevels", niveauScolaireService.getAll());
        model.addAttribute("levelExperiences", niveauExperienceService.getAll());
        model.addAttribute("typeOffres", typeOffreService.getAll());
        model.addAttribute("companies", companyService.getAll());
        model.addAttribute("offer", data);
        return templateFileUrl+"/save";
    }

    @PostMapping("/save")
    public String submitSaveOfferFormFromBackend(
            Model model,
            HttpServletRequest request,
            Authentication authentication,
            @ModelAttribute Offer data,
            @RequestParam("dateExpirationInString") String dateExpirationInString,
            @RequestParam("file") MultipartFile file
    ) throws Exception {
        User authenticatedUser = userService.getOneByUsername(authentication.getName());
        model.addAttribute("authenticatedUser", authenticatedUser);

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

        // on determine si on a affaire a une creation ou a une modification en verifiant si l'id de l'offre !+ null
        Offer resultOp = null;
        if(data.getId() == null || data.getId().toString().isBlank() || data.getId().toString().equals("-1")) {
            // creation
            resultOp = offerService.create(data);
        } else {
            // modification
            resultOp = offerService.update(data.getId().toString(), data);
        }

        if(resultOp != null) {
            // si l'enregistrement a reussi
            return "redirect:/backend/offer/list";
        }

        // si l'enregistrement a echoue, on redirige sur la page precedente
        String referer = request.getHeader("Referer");
        return "redirect:"+ referer;
    }
}
