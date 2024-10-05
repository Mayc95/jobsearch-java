package com.maycdevs.jobsearch.controller.frontend;

import com.maycdevs.jobsearch.api.FilesApiController;
import com.maycdevs.jobsearch.model.Attachement;
import com.maycdevs.jobsearch.model.Offer;
import com.maycdevs.jobsearch.model.User;
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
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

@Slf4j
@Controller
@RequestMapping("/account")
public class UserAccountFrontendController {

    @Autowired
    private UserService userService;
    @Autowired
    private AttachementService manageFilesService;
    @Autowired
    private FileStorageService fileStorageService;
    @Autowired
    private ActivitySectorService activitySectorService;
    @Autowired
    private OfferService offerService;



    private String baseControllerUrl = "account";
    private String templateFileUrl = "frontend/account";


    @GetMapping("/connexion/{result}/{action}")
    public String urlForRedirectionToCorrectUserPage(
            Model model,
            Authentication authentication,
            @PathVariable("result") @DefaultValue("success") String result,
            @PathVariable("action") @DefaultValue("redirection") String action
    ) throws Exception {
        // on recupere les donnees de l'user qui est connecter
        User authenticatedUser = null;
        if(authentication != null && authentication.getName() != null) {
            authenticatedUser = userService.getOneByUsername(authentication.getName());
        }

        if(authenticatedUser != null) {
            if(result.equals("success")) {
                if(authenticatedUser.getRole().getLibelle().equals("admin")) {
                    return "redirect:/backend";
                } else {
                    return "redirect:/account/profile";
                }
            }
        }

        return "redirect:/login";
    }

    @GetMapping({"","/","/profile"})
    public String userAccountDashboardPage(
            Model model,
            Authentication authentication
    ) throws Exception {
        // on recupere les donnees de l'user qui est connecter
        User authenticatedUser = null;
        if(authentication != null && authentication.getName() != null) {
            authenticatedUser = userService.getOneByUsername(authentication.getName());
        }

        model.addAttribute("authenticatedUser",authenticatedUser);
        model.addAttribute("allActivitySector",activitySectorService.getAll());
        model.addAttribute("allFormations",activitySectorService.getAll());
        model.addAttribute("activeAccountSidebarLink","/account");

        return templateFileUrl+"/profile";

        /*
        model.addAttribute("authenticatedUser",null);
        return "frontend/login";
        */
    }

    @GetMapping("/password")
    public String changeUserPasswordPage(Model model, Authentication authentication) throws Exception {
        // on recupere les donnees de l'user qui est connecter
        User authenticatedUser = null;
        if(authentication != null && authentication.getName() != null) {
            authenticatedUser = userService.getOneByUsername(authentication.getName());
        }

        model.addAttribute("authenticatedUser",authenticatedUser);
        return templateFileUrl+"/password";
    }

    @PostMapping("/profile")
    public String submitUpdateUserProfileForm(
            @ModelAttribute User utilisateur,
            @RequestParam("datenaissance") String datenaissance,
            @RequestParam("file") MultipartFile file
    ) throws Exception {
        // Gestion du fichier si la valeur de son parametre est != null
        if(file != null && !Objects.requireNonNull(file.getOriginalFilename()).isBlank() && !file.isEmpty()) {
            /*
            Attachement uploadedFile = manageFilesService.storeInDB(file);
            utilisateur.setPhoto(uploadedFile);
            */
            fileStorageService.store(file);
            Path uploadedFilePath = fileStorageService.load(file.getOriginalFilename());
            String fileLocation = MvcUriComponentsBuilder.fromMethodName(FilesApiController.class,
                    "displayFile", file.getOriginalFilename()).build().toUri().toString();
            utilisateur.setPhotoLink(fileLocation);

        }

        // on converti la valeur de la variable dateExpirationInString en type Date
        if(!datenaissance.isBlank()) {
            Date birthdate = new SimpleDateFormat("yyyy-MM-dd").parse(datenaissance);
            utilisateur.setBirthdate(birthdate);
        }

        // modifier les informations de l'user
        userService.update(utilisateur.getId().toString(), utilisateur);

        return "redirect:/account/profile";
    }

    @PostMapping("/password")
    public String subimitChangeUserPasswordForm(
            @RequestParam("id") String id,
            @RequestParam("oldpassword") String oldpassword,
            @RequestParam("newpassword") String newpassword,
            @RequestParam("confpassword") String confpassword
    ) throws Exception {
        User utilisateur = userService.getOneById(id);
        if(utilisateur != null) {
            // changer le mot de passe de l'user
            Boolean result = userService.changePassword(id,oldpassword,newpassword,confpassword);
            if(result) {
                return templateFileUrl;
            }
        }

        return templateFileUrl+"/account/password";
    }

    @GetMapping("/manage/offers")
    public String showListOfOffersCreatedByAuthenticatedUser(
            Model model,
            Authentication authentication
    ) throws Exception {
        // on recupere les donnees de l'user qui est connecter
        User authenticatedUser = null;
        if(authentication != null && authentication.getName() != null) {
            authenticatedUser = userService.getOneByUsername(authentication.getName());
        }

        if(authenticatedUser != null) {
            if(authenticatedUser.getRole().getLibelle().equalsIgnoreCase("candidat")) {
                // si l'user connecter est un candidat, alors il n'a pas acces a cette page
                // donc on le redirige sur la page d'accueil de son compte
                return "redirect:/account/profile";
            }
        } else {
            // Aucun user connecter, on redirige vers la page de connexion
            return "redirect:/login";
        }

        // Si on arrive ici c'est que un utilisateur est connecter et ce utilisateur n'est pas un candidat
        // alors on recupere la liste des offres creer par l'utilisateur
        List<Offer> data = new ArrayList<>();
        User finalAuthenticatedUser = authenticatedUser;
        offerService.getAll().forEach(offer -> {
            User currentOfferIsAuthor = offer.getAuthor();
            if(currentOfferIsAuthor!=null && currentOfferIsAuthor.getId().equals(finalAuthenticatedUser.getId())) {
                data.add(offer);
            }
        });

        model.addAttribute("authenticatedUser", finalAuthenticatedUser);
        model.addAttribute("allOffers", data);
        return templateFileUrl+"/recruteur/manage_offers";
    }
}
