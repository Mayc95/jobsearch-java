package com.maycdevs.jobsearch.api;

import com.maycdevs.jobsearch.model.NiveauScolaire;
import com.maycdevs.jobsearch.service.NiveauScolaireService;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping(value = "/api/v1/niveauscolaire")
public class NiveauScolaireApiController {

    @Autowired
    private NiveauScolaireService niveauScolaireService;

    @GetMapping({"","/"})
    public List<NiveauScolaire> listAllNiveauxScolaires() {
        return niveauScolaireService.getAll();
    }

    @PostMapping({"","/"})
    public NiveauScolaire createNiveauScolaire(@RequestBody @NonNull NiveauScolaire niveau) throws Exception {
        return niveauScolaireService.create(niveau);
    }

    @PutMapping("/{id}")
    public NiveauScolaire updateNiveauScolaire(
            @PathVariable String id,
            @RequestBody @NonNull NiveauScolaire niveau
    ) throws Exception {
        return niveauScolaireService.update(id, niveau);
    }

    @DeleteMapping("/{id}")
    public Boolean deleteNiveauScolaire(@PathVariable String id) throws Exception {
        return niveauScolaireService.delete(id);
    }
}
