package com.maycdevs.jobsearch.api;

import com.maycdevs.jobsearch.model.NiveauExperience;
import com.maycdevs.jobsearch.service.NiveauExperienceService;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping(value = "/api/v1/niveauexperience")
public class NiveauExperienceApiController {

    @Autowired
    private NiveauExperienceService niveauExperienceService;

    @GetMapping({"","/"})
    public List<NiveauExperience> listAllNiveauExperience() {
        return niveauExperienceService.getAll();
    }

    @PostMapping({"","/"})
    public NiveauExperience createNiveauExperience(@RequestBody @NonNull NiveauExperience niveau) throws Exception {
        return niveauExperienceService.create(niveau);
    }

    @PutMapping("/{id}")
    public NiveauExperience updateNiveauExperience(
            @PathVariable String id,
            @RequestBody @NonNull NiveauExperience niveau
    ) throws Exception {
        return niveauExperienceService.update(id, niveau);
    }

    @DeleteMapping("/{id}")
    public Boolean deleteNiveauExperience(@PathVariable String id) throws Exception {
        return niveauExperienceService.delete(id);
    }

}
