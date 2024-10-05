package com.maycdevs.jobsearch.api;


import com.maycdevs.jobsearch.model.TypeOffre;
import com.maycdevs.jobsearch.service.TypeOffreService;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping(value = "/api/v1/typeoffre")
public class TypeOffreApiController {

    @Autowired
    private TypeOffreService typeOffreService;

    @GetMapping(value = {"","/"})
    public List<TypeOffre> listAllTypeOffres() {
        return typeOffreService.getAll();
    }

    @PostMapping(value = {"","/"})
    public TypeOffre createTypeOffre(@RequestBody @NonNull TypeOffre typeoffre) throws Exception {
        return typeOffreService.create(typeoffre);
    }

    @GetMapping("/{id}")
    public TypeOffre getTypeOffre(@PathVariable String id) throws Exception {
        return typeOffreService.getOneById(id);
    }

    @PutMapping("/{id}")
    public TypeOffre updateTypeOffre(
            @PathVariable final String id,
            @RequestBody TypeOffre typeoffre
    ) throws Exception {
        return typeOffreService.update(id, typeoffre);
    }

    @DeleteMapping("/{id}")
    public Boolean deleteTypeOffre(@PathVariable final String id) throws Exception {
        return typeOffreService.delete(id);
    }
}
