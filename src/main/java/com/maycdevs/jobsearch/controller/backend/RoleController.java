package com.maycdevs.jobsearch.controller.backend;


import com.maycdevs.jobsearch.model.Role;
import com.maycdevs.jobsearch.service.RoleService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

@Slf4j
@Controller
@RequestMapping("/backend/config/roles")
public class RoleController {

    @Autowired
    private RoleService roleService;

    private String baseControllerUrl = "backend/config/roles";
    private String templateFilePath = "backend/config/roles";

    @GetMapping(value = {"","/list"})
    public String manageUserRoles(Model model) {
        model.addAttribute("roles", roleService.getAll());
        return templateFilePath;
    }

    @PostMapping("/save")
    public ModelAndView saveRole(@ModelAttribute Role r) throws Exception {
        Long id = r.getId();
        if(id != null) {
            roleService.update(id.toString(), r);
        } else {
            roleService.create(r);
        }

        return new ModelAndView("redirect:/"+baseControllerUrl);
    }

    @PostMapping("/delete")
    public ModelAndView deleteRole(@RequestParam("id") String id) throws Exception {
        Boolean result = roleService.delete(id);
        log.debug("result delete Role with id="+id+" is: "+result);
        return new ModelAndView("redirect:/"+baseControllerUrl);
    }
}
