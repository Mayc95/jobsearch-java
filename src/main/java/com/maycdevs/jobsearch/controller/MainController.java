package com.maycdevs.jobsearch.controller;

import com.maycdevs.jobsearch.model.User;
import com.maycdevs.jobsearch.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;

import java.util.Objects;

public class MainController {

    @Autowired
    private UserService userService;

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

}
