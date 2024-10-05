package com.maycdevs.jobsearch.controller.backend;

import com.maycdevs.jobsearch.api.FilesApiController;
import com.maycdevs.jobsearch.controller.MainController;
import com.maycdevs.jobsearch.model.Attachement;
import com.maycdevs.jobsearch.model.Company;
import com.maycdevs.jobsearch.service.ActivitySectorService;
import com.maycdevs.jobsearch.service.AttachementService;
import com.maycdevs.jobsearch.service.CompanyService;
import com.maycdevs.jobsearch.service.FileStorageService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.method.annotation.MvcUriComponentsBuilder;

import java.nio.file.Path;
import java.util.Objects;

@Slf4j
@Controller
@RequestMapping("/backend/config/company")
public class CompanyBackendController extends MainController {

    @Autowired
    private CompanyService companyService;
    @Autowired
    private AttachementService manageFilesService;
    @Autowired
    private ActivitySectorService activitySectorService;
    @Autowired
    private FileStorageService fileStorageService;

    private String baseControllerUrl = "backend/config/company";
    private String templateFileUrl = "backend/config/company";

    @GetMapping({"","/","/list"})
    public String manageCompanies(Model model, Authentication authentication) throws Exception {
        model.addAttribute("allCompanies", companyService.getAll());
        return templateFileUrl+"/list";
    }

    @GetMapping("/add")
    public String addCompany(Model model) {
        model.addAttribute("allActivitySector", activitySectorService.getAll());
        return templateFileUrl+"/add";
    }

    @PostMapping("/update")
    public String updateCompany(
            Authentication authentication,
            Model model,
            @RequestParam("id") String id
    ) throws Exception {
        Company c = companyService.getOneById(id);
        if(c != null) {
            model.addAttribute("company", c);
            model.addAttribute("allActivitySector", activitySectorService.getAll());
            return templateFileUrl+"/update";
        }

        return manageCompanies(model, authentication);
    }

    @PostMapping("/save")
    public ModelAndView saveCompany(
            @ModelAttribute Company company,
            @RequestParam("file") MultipartFile file
    ) throws Exception {
        // Gestion du fichier si la valeur de son parametre est != null
        if(file != null && !Objects.requireNonNull(file.getOriginalFilename()).isBlank() && !file.isEmpty()) {
            /* store image in db
            Attachement uploadedFile = manageFilesService.storeInDB(file);
            company.setLogo(uploadedFile);
            */

            // store image in project directory
            fileStorageService.store(file);
            Path uploadedFilePath = fileStorageService.load(file.getOriginalFilename());
            String fileLocation = MvcUriComponentsBuilder.fromMethodName(FilesApiController.class,
                    "displayFile", file.getOriginalFilename()).build().toUri().toString();
            company.setLogoLocation(fileLocation);
        }

        Company result = null;
        if(company.getId() != null) {
            result = companyService.update(company.getId().toString(), company);
        } else {
            result = companyService.create(company);
        }

        // supprimer l'ancien logo lorsque il y a modification d'une company avec un nouveau logo
        // supprimer le nouveau fichier enregistrer lorsqu'il y a erreur sur lors de l'enregistrement de la company

        return new ModelAndView("redirect:/"+baseControllerUrl);
    }

    @PostMapping("/delete")
    public ModelAndView deleteCompany(@RequestParam("id") String id) throws Exception {
        Boolean result = companyService.delete(id);
        log.debug(result.toString());
        return new ModelAndView("redirect:/"+baseControllerUrl);
    }
}
