package com.maycdevs.jobsearch.controller.backend;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;


@Slf4j
@Controller
@RequestMapping(value = "/backend")
public class BackendController {

    @GetMapping(value = {"","/","/home","/dashboard"})
    public String dashboard() {
        return "backend/dashboard";
    }

}
