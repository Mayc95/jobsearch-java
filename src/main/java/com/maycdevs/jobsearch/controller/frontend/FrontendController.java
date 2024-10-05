package com.maycdevs.jobsearch.controller.frontend;


import com.maycdevs.jobsearch.model.Role;
import com.maycdevs.jobsearch.model.User;
import com.maycdevs.jobsearch.service.ActivitySectorService;
import com.maycdevs.jobsearch.service.CompanyService;
import com.maycdevs.jobsearch.service.RoleService;
import com.maycdevs.jobsearch.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

@Slf4j
@Controller
public class FrontendController {

    @Autowired
    private UserService userService;
    @Autowired
    private ActivitySectorService activitySectorService;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private RoleService roleService;
    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private UserAccountFrontendController userAccountFrontendController;
    @Autowired
    private CompanyService companyService;


    private String baseControllerUrl = "";
    private String templateFileUrl = "frontend";

    public FrontendController() {
    }

    @GetMapping({"","/","/home"})
    public String homePage(Model model, Authentication authentication) throws Exception {
        // on recupere les donnees de l'user qui est connecter
        User authenticatedUser = null;
        if(authentication != null && authentication.getName() != null) {
            authenticatedUser = userService.getOneByUsername(authentication.getName());
        }

        model.addAttribute("authenticatedUser",authenticatedUser);
        model.addAttribute("allActivitySector", activitySectorService.getAll());
        model.addAttribute("allCompanies", companyService.getAll());
        model.addAttribute("activeHeaderLink", "/");
        return templateFileUrl+"/home";
    }

    @GetMapping("/about")
    public String aboutPage(Model model, Authentication authentication) throws Exception {
        // on recupere les donnees de l'user qui est connecter
        User authenticatedUser = null;
        if(authentication != null && authentication.getName() != null) {
            authenticatedUser = userService.getOneByUsername(authentication.getName());
        }

        model.addAttribute("authenticatedUser",authenticatedUser);
        model.addAttribute("activeHeaderLink", "/about");
        return templateFileUrl+"/about";
    }

    @GetMapping("/contact")
    public String contactPage(Model model, Authentication authentication) throws Exception {
        // on recupere les donnees de l'user qui est connecter
        User authenticatedUser = null;
        if(authentication != null && authentication.getName() != null) {
            authenticatedUser = userService.getOneByUsername(authentication.getName());
        }

        model.addAttribute("authenticatedUser",authenticatedUser);
        model.addAttribute("activeHeaderLink", "/contact");
        return templateFileUrl+"/contact";
    }

    @GetMapping("/register")
    public String registerPage(Model model, Authentication authentication) throws Exception {
        // on recupere les donnees de l'user qui est connecter
        User authenticatedUser = null;
        if(authentication != null && authentication.getName() != null) {
            authenticatedUser = userService.getOneByUsername(authentication.getName());
        }

        model.addAttribute("authenticatedUser",authenticatedUser);
        model.addAttribute("allActivitySector", activitySectorService.getAll());
        return templateFileUrl+"/register";
    }

    @PostMapping("/register")
    public ModelAndView submitRegisterForm(
            @ModelAttribute User newUser,
            @RequestParam("typeuser") String typeuser
    ) throws Exception {
        if(newUser.getUsername() == null) {
            newUser.setUsername(newUser.getEmail());
        }
        User u = userService.getOneByUsername(newUser.getUsername());

        if(u == null) {
            Role userRole = null;
            // on fait la creation du nouvel utilisateur
            if(typeuser.equalsIgnoreCase("candidat")) {
                userRole = roleService.getOneByLibelle("candidat");
            } else if (typeuser.equalsIgnoreCase("recruteur")) {
                userRole = roleService.getOneByLibelle("recruteur");
            }

            // on enregistre le role de l'user
            if(userRole != null) {
                newUser.setRole(userRole);
            }

            // on encode le mot de passe
            newUser.setPassword(passwordEncoder.encode(newUser.getPassword()));

            // on enregistre le nouveau user
            u = userService.create(newUser);
            if(u != null) {
                return new ModelAndView("redirect:/login?success");
            } else {
                // gestion des erreurs
            }
            return new ModelAndView("redirect:/register?error");
        }

        // un user ayant le meme username existe deja, alors on redirige l'user sur la page de login
        return new ModelAndView("redirect:/login");

    }

    @GetMapping("/login")
    public String loginPage(Model model, Authentication authentication) throws Exception {
        // on recupere les donnees de l'user qui est connecter
        User authenticatedUser = null;
        if(authentication != null && authentication.getName() != null) {
            authenticatedUser = userService.getOneByUsername(authentication.getName());
        }

        model.addAttribute("authenticatedUser",authenticatedUser);
        model.addAttribute("activeHeaderLink", "/login");
        return templateFileUrl+"/login";
    }


    /*
    @PostMapping("/login")
    public String submitLoginForm(
            Model model,
            @RequestParam("username") String username,
            @RequestParam("password") String password
    ) throws Exception {
        if (username != null && password != null && !username.isBlank() && !password.isBlank()) {
            try {
                Authentication result = authenticationManager.authenticate(
                                new UsernamePasswordAuthenticationToken(username, password)
                );
                log.debug(String.valueOf(result.isAuthenticated()));
                if(result.isAuthenticated()) {
                    User authenticatedUser = userService.getOneByUsername(username);
                    model.addAttribute("authenticatedUser", authenticatedUser);
                }
                return userAccountFrontendController.userAccountDashboardPage(model, result);
            } catch (Exception e) {
                log.debug(e.toString());
                e.printStackTrace();
                throw new Exception("Could not authenticate User with username: " + username + " in database because: " + e.toString());
            }
        }
        model.addAttribute("error", "Oui il y a erreur");
        return templateFileUrl+"/login";
    }

    */
}
