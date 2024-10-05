package com.maycdevs.jobsearch.service;

import com.maycdevs.jobsearch.model.ActivitySector;
import com.maycdevs.jobsearch.model.Attachement;
import com.maycdevs.jobsearch.model.Company;
import com.maycdevs.jobsearch.repository.CompanyRepository;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Slf4j
@Service
public class CompanyService implements EntityCRUDService<Company> {

    @Autowired
    private CompanyRepository companyRepository;
    @Autowired
    private ActivitySectorService activitySectorService;

    public List<Company> getAll() {
        return companyRepository.findAll();
    }

    public Company getOneById(String idInString) throws Exception {
        try {
            Long id = Long.parseLong(idInString);
            return companyRepository.findById(id).orElse(null);
        } catch (Exception e) {
            log.debug(e.toString());
            e.printStackTrace();
            throw e;
        }
    }

    public Company create(@NonNull Company data) throws Exception {
        // verification des valeurs des proprietes de l'objet level
        if(data.getActivitySector() != null) {
            ActivitySector sector = activitySectorService.getOneById(data.getActivitySector().getId().toString());
            if(sector != null) {
                try {
                    return companyRepository.save(data);
                } catch (Exception e) {
                    log.debug(e.toString());
                    e.printStackTrace();
                    throw e;
                }
            }
        }

        return null;
    }

    public Company update(String id, Company data) throws Exception {
        Company current = getOneById(id);
        if(current != null) {
            try {
                String name = data.getName();
                if(name != null) {
                    current.setName(name);
                }

                String desc = data.getDescription();
                if(desc != null) {
                    current.setDescription(desc);
                }

                Date datecreation = data.getDateDeCreation();
                if(datecreation != null) {
                    current.setDateDeCreation(datecreation);
                }

                String link = data.getWebsitelink();
                if(link != null && !link.isBlank()) {
                    current.setWebsitelink(link);
                }

                if(data.getActivitySector() != null) {
                    ActivitySector sector = activitySectorService.getOneById(data.getActivitySector().getId().toString());
                    if(sector != null ) {
                        current.setActivitySector(sector);
                    }
                }

                Attachement newLogo = data.getLogo();
                if (newLogo != null) {
                    current.setLogo(newLogo);
                }
                String newLogoLocation = data.getLogoLocation();
                if (newLogoLocation != null) {
                    current.setLogoLocation(newLogoLocation);
                }

                return companyRepository.save(current);

            } catch (Exception e) {
                log.debug(e.toString());
                e.printStackTrace();
                throw e;
            }
        }

        return null;
    }

    public Boolean delete(String id) throws Exception {
        Company company = getOneById(id);
        if(company != null) {
            try {
                companyRepository.delete(company);
                return true;
            } catch (Exception e) {
                log.debug(e.toString());
                e.printStackTrace();
                throw e;
            }

        }
        return false;
    }
}
