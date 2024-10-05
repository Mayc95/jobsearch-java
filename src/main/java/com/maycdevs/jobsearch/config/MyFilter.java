package com.maycdevs.jobsearch.config;

import com.maycdevs.jobsearch.model.User;
import com.maycdevs.jobsearch.service.UserService;
import jakarta.servlet.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.io.IOException;

public class MyFilter implements Filter {

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

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        // Votre logique ici
        System.out.println("Avant la requête, execution methode 'doFilter' de la class 'MyFilter'");
        filterChain.doFilter(servletRequest, servletResponse); // Continuer avec la chaîne de filtres
        System.out.println("Après la requête et l'execution de la methode 'doFilter' de la class 'MyFilter'");
        /*
        // retrive Authentication
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        try {
            String r = isUserHasAccess(authentication);
            System.out.println("Result of 'isUserHasAccess' method is : "+r);
            System.out.println(" ");
            filterChain.doFilter(servletRequest, servletResponse); // Continuer avec la chaîne de filtres
            System.out.println("Après la requête et l'execution de la methode 'doFilter' de la class 'MyFilter'");
        } catch (Exception e) {
            System.out.println("Erreur pendant execution de la methode 'isUserHasAccess' de la class 'MyFilter'");
            throw new RuntimeException(e);
        }

        // Chercher comment Changer l'url actuelle pour pouvoir rediriger l'user vers une autre page
        // sinon impossible d'utiliser la methode avec Aspect pour resoudre mon probleme
        // lier a la redirection de l'user vers une autre page en fonction de son access a la page demander

        */
    }
}
