package com.maycdevs.jobsearch.controller.backend;

import com.maycdevs.jobsearch.model.NiveauExperience;
import com.maycdevs.jobsearch.service.NiveauExperienceService;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

@Slf4j
@Controller
@RequestMapping(value = "/backend/config/niveauexperience")
public class NiveauExperienceController {

    @Autowired
    private NiveauExperienceService niveauExperienceService;

    private String baseControllerUrl = "backend/config/niveauexperience";
    private String templateFileUrl = "backend/config/niveau_experience";

    @GetMapping({"","/","/list"})
    public String manageNiveauExperience(Model model) {
        model.addAttribute("niveaux", niveauExperienceService.getAll());
        return templateFileUrl;
    }

    @PostMapping("/save")
    public ModelAndView saveNiveauExperience(@ModelAttribute @NonNull NiveauExperience niveau) throws Exception {
        Long id = niveau.getId();
        if(id != null) {
            niveauExperienceService.update(id.toString(), niveau);
        } else {
            niveauExperienceService.create(niveau);
        }

        return new ModelAndView("redirect:/"+baseControllerUrl);
    }

    @PostMapping("/delete")
    public ModelAndView deleteNiveauExperience(String id) throws Exception {
        Boolean result = niveauExperienceService.delete(id);
        log.debug("result delete Niveau experience with id="+id+" is: "+result);
        return new ModelAndView("redirect:/"+baseControllerUrl);
    }
}
