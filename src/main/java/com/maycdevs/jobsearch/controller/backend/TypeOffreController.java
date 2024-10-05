package com.maycdevs.jobsearch.controller.backend;

import com.maycdevs.jobsearch.model.TypeOffre;
import com.maycdevs.jobsearch.service.TypeOffreService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

@Slf4j
@Controller
@RequestMapping(value = "/backend/config/typeoffre")
public class TypeOffreController {

    @Autowired
    private TypeOffreService typeOffreService;

    private String baseControllerUrl = "backend/config/typeoffre";
    private String templateFilePath = "backend/config/type_offre";

    @GetMapping({"","/list"})
    public String manageTypeOffre(Model model) {
        model.addAttribute("typesoffres", typeOffreService.getAll());
        return templateFilePath;
    }

    @PostMapping("/save")
    public ModelAndView saveTypeOffre(
            @ModelAttribute TypeOffre typeOffre
    ) throws Exception {
        Long id = typeOffre.getId();
        if(id != null) {
            typeOffreService.update(id.toString(), typeOffre);
        } else {
            typeOffreService.create(typeOffre);
        }
        return new ModelAndView("redirect:/"+baseControllerUrl);
    }

    @PostMapping("/delete")
    public ModelAndView deleteTypeOffre(
            @RequestParam("id") String id
    ) throws Exception {
        Boolean result = typeOffreService.delete(id);
        log.debug("result delete Type offre with id="+id+" is: "+result);
        return new ModelAndView("redirect:/"+baseControllerUrl);
    }

}
