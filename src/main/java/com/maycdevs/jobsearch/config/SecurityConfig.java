package com.maycdevs.jobsearch.config;

import com.maycdevs.jobsearch.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.LogoutConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.DefaultSecurityFilterChain;
import org.springframework.security.web.SecurityFilterChain;


@Configuration
@RequiredArgsConstructor
public class SecurityConfig {

    private final UserService userDetailsService;

    // permet de definir un ou des filtres sur les requettes https
    // les filtres vont etre executer a chaque fois qu'une requette http est ....

    @Bean
    public SecurityFilterChain httpSecurityFilterChain(HttpSecurity httpSecurity) throws Exception {
        return httpSecurity
                // delete this line before production build
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(auth ->
                        auth.requestMatchers(
                                "/assets/**","/","/home","/about","/contact","/register","/login",
                                "/offer","/offer/","/offer/**", "/api/v1/**"
                        ).permitAll()
                         .anyRequest().authenticated()
                )
                .formLogin((form) -> form
                        .loginPage("/login")
                        .defaultSuccessUrl("/account/connexion/success/redirection", true)
                        .permitAll()
                )
                .logout(LogoutConfigurer::permitAll)
                .build();

    }



    public WebSecurityCustomizer webSecurityConfigCustomizer() throws Exception {
        // autoriser les fichiers css et es assets
        return web -> web.ignoring().requestMatchers(
                "/assets/**",
                "/login/**",
                "/css/**",
                "/demo/**",
                "/fonts/**",
                "/img/**",
                "/js/**",
                "/bundles/**",
                "/scss/**",
                "/upload/**"
        );
    }



    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(HttpSecurity httpSecurity, PasswordEncoder passwordEncoder) throws Exception {
        AuthenticationManagerBuilder authenticationManagerBuilder = httpSecurity.getSharedObject(AuthenticationManagerBuilder.class);
        authenticationManagerBuilder.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder);
        return authenticationManagerBuilder.build();
    }
}
