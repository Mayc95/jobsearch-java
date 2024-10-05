package com.maycdevs.jobsearch.controller.backend;

import com.maycdevs.jobsearch.model.NiveauExperience;
import com.maycdevs.jobsearch.model.NiveauScolaire;
import com.maycdevs.jobsearch.service.NiveauExperienceService;
import com.maycdevs.jobsearch.service.NiveauScolaireService;
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

@Slf4j
@Controller
@RequestMapping(value = "/backend/config/niveauscolaire")
public class NiveauScolaireController {

    @Autowired
    private NiveauScolaireService niveauScolaireService;

    private String baseControllerUrl = "backend/config/niveauscolaire";
    private String templateFileUrl = "backend/config/niveau_scolaire";

    @GetMapping({"","/","/list"})
    public String manageNiveauScolaire(Model model) {
        model.addAttribute("niveaux", niveauScolaireService.getAll());
        return templateFileUrl;
    }

    @PostMapping("/save")
    public ModelAndView saveNiveauScolaire(@ModelAttribute @NonNull NiveauScolaire niveau) throws Exception {
        Long id = niveau.getId();
        if(id != null) {
            niveauScolaireService.update(id.toString(), niveau);
        } else {
            niveauScolaireService.create(niveau);
        }

        return new ModelAndView("redirect:/"+baseControllerUrl);
    }

    @PostMapping("/delete")
    public ModelAndView deleteNNiveauScolaire(String id) throws Exception {
        Boolean result = niveauScolaireService.delete(id);
        log.debug("result delete Niveau scolaire with id="+id+" is: "+result);
        return new ModelAndView("redirect:/"+baseControllerUrl);
    }
}
