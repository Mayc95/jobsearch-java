package com.maycdevs.jobsearch.controller.backend;


import com.maycdevs.jobsearch.model.ActivitySector;
import com.maycdevs.jobsearch.service.ActivitySectorService;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

@Slf4j
@Controller
@RequestMapping(value = "/backend/config/activitysector")
public class ActivitySectorBackendController {

    @Autowired
    private ActivitySectorService activitySectorService;

    private String baseControllerUrl = "backend/config/activitysector";
    private String templateFilePath = "backend/config/activity_sector";

    @GetMapping({"","/","/list"})
    public String manageActivitySector(Model model) {
        model.addAttribute("sectors", activitySectorService.getAll());
        return templateFilePath;
    }

    @PostMapping("/save")
    public ModelAndView saveActivitySector(
            @ModelAttribute @NonNull ActivitySector sector
    ) throws Exception {
        Long id = sector.getId();
        if(id != null) {
            activitySectorService.update(id.toString(), sector);
        } else {
            activitySectorService.create(sector);
        }
        return new ModelAndView("redirect:/"+baseControllerUrl);
    }

    @PostMapping("/delete")
    public ModelAndView deleteActivitySector(
            Model model,
            @RequestParam("id") String id
    ) throws Exception {
        Boolean result = activitySectorService.delete(id);
        log.debug("result delete Activity sector with id="+id+" is: "+result);
        return new ModelAndView("redirect:/"+baseControllerUrl);
    }

}
