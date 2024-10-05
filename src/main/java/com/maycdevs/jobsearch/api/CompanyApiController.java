package com.maycdevs.jobsearch.api;


import com.maycdevs.jobsearch.model.Company;
import com.maycdevs.jobsearch.service.ActivitySectorService;
import com.maycdevs.jobsearch.service.CompanyService;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/v1/company")
public class CompanyApiController {

    @Autowired
    private CompanyService companyService;

    @GetMapping({"","/"})
    public List<Company> listAllCompany() {
        return companyService.getAll();
    }

    @PostMapping({"","/"})
    public Company createCompany(@RequestBody @NonNull Company company) throws Exception {
        return companyService.create(company);
    }

    @PutMapping("/{id}")
    public Company updateCompany(
            @PathVariable String id,
            @RequestBody Company company
    ) throws Exception {
        return companyService.update(id, company);
    }

    @DeleteMapping("/{id}")
    public Boolean deleteCompany(@PathVariable String id) throws Exception {
        return companyService.delete(id);
    }
}
