package com.maycdevs.jobsearch.config;

import com.maycdevs.jobsearch.model.User;
import com.maycdevs.jobsearch.service.UserService;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;


@Aspect
public class MyAspect {

    @Autowired
    private UserService userService;

    public String isUserHasAccess() throws Exception {
        User authenticatedUser = null;
        String text="";
        // retrive Authentication
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if(authentication != null && !authentication.getName().isBlank()) {
            authenticatedUser = userService.getOneByUsername(authentication.getName());
            if(authenticatedUser != null) {
                if (authenticatedUser.getRole().getLibelle().equalsIgnoreCase("admin")) {
                    // si l'user connecter est un admin
                    text = "true";
                } else {
                    // si l'user connecter est un candidat ou un recruteur, alors il n'a pas acces a cette page
                    // on le deconnecte
                    text = "redirect:/account/profile";
                }
            } else {
                // Aucun user connecter, on redirige vers la page de connexion
                text = "redirect:/login";
            }
        } else {
            // Aucun user connecter, on redirige vers la page de connexion
            text = "redirect:/login";
        }

        // Chercher comment Changer l'url actuelle pour pouvoir rediriger l'user vers une autre page
        // sinon impossible d'utiliser la methode avec Aspect pour resoudre mon probleme
        // lier a la redirection de l'user vers une autre page en fonction de son access a la page demander
        return text;
    }

    @Before("execution(* com.maycdevs.jobsearch.controller..*(..))") // Remplacez par votre package
    public String beforeControllerMethods() throws Exception {
        // Votre logique ici
        System.out.println("Avant la méthode du contrôleur, execution methode 'beforeControllerMethods' de la class 'MyAspect'");
        return isUserHasAccess();
    }

}
