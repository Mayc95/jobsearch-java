package com.maycdevs.jobsearch.config;

import com.maycdevs.jobsearch.model.User;
import com.maycdevs.jobsearch.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

public class MyInterceptor implements HandlerInterceptor {

    @Autowired
    private UserService userService;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        User authenticatedUser = null;
        String text="";
        String currentUrl = request.getRequestURI();

        // repertorier les url sur lesquelles je dois verifier le role des user et utiliser switch

        if(!currentUrl.contains("assets") && !currentUrl.equalsIgnoreCase("/login")) {
            // retrive Authentication
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

            if(authentication != null && !authentication.getName().isBlank()) {
                authenticatedUser = userService.getOneByUsername(authentication.getName());
                if(authenticatedUser != null) {
                    if(currentUrl.contains("backend")) {
                        // les acces et redirection au backend
                        if (authenticatedUser.getRole().getLibelle().equalsIgnoreCase("admin")) {
                            // si l'user connecter est un admin
                            text = "true";
                        } else {
                            // si l'user connecter est un candidat ou un recruteur, alors il n'a pas acces a cette page
                            // on le deconnecte
                            text = "/account/profile";
                        }
                    } else {
                        // les acces et redirection au frontend
                        if (authenticatedUser.getRole().getLibelle().equalsIgnoreCase("admin")) {
                            // si l'user connecter est un admin alors il n'a pas acces a cette page
                            text = "/backend";
                        } else {
                            // si l'user connecter est un candidat ou un recruteur, alors il a acces a cette page
                            text = "true";
                        }
                    }
                } else {
                    // Aucun user connecter, on redirige vers la page de connexion
                    text = "/login";
                }
            } else {
                // Aucun user connecter, on redirige vers la page de connexion
                text = "/login";
            }

            if(!text.equalsIgnoreCase("true")) {
                // on modifie l'url de la reponse pour rediriger l'user sur une autre page
                String encodedRedirectURL = response.encodeRedirectURL(request.getContextPath() + text);
                response.sendRedirect(encodedRedirectURL);
                //response.setStatus(HttpStatusCode);
                //response.setHeader("Location", encodedRedirectURL);
            }
        }

        /*
        -- gestion acces pour backend seulement
        if(!currentUrl.contains("assets") && currentUrl.contains("backend")) {
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
                        text = "/account/profile";
                    }
                } else {
                    // Aucun user connecter, on redirige vers la page de connexion
                    text = "/login";
                }
            } else {
                // Aucun user connecter, on redirige vers la page de connexion
                text = "/login";
            }

            if(!text.equalsIgnoreCase("true")) {
                // on modifie l'url de la reponse pour rediriger l'user sur une autre page
                String encodedRedirectURL = response.encodeRedirectURL(request.getContextPath() + text);
                response.sendRedirect(encodedRedirectURL);
                //response.setStatus(HttpStatusCode);
                //response.setHeader("Location", encodedRedirectURL);
            }
        }
        */

        return true;
    }

}
