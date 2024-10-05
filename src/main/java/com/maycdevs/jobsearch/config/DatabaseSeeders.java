package com.maycdevs.jobsearch.config;

import com.maycdevs.jobsearch.model.*;
import com.maycdevs.jobsearch.service.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Configuration;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Slf4j
@Configuration
@RequiredArgsConstructor
public class DatabaseSeeders implements ApplicationRunner {

    @Autowired
    private final ActivitySectorService activitySectorService;
    @Autowired
    private final RoleService roleService;
    @Autowired
    private final TypeOffreService typeOffreService;
    @Autowired
    private final NiveauScolaireService niveauScolaireService;
    @Autowired
    private final NiveauExperienceService niveauExperienceService;
    @Autowired
    private final CompanyService companyService;


    @Override
    public void run(ApplicationArguments args) throws Exception {
        if(args.getOptionValues("seeder") != null){
            List<String> seeder = Arrays.asList(args.getOptionValues("seeder").get(0).split(","));
            if(seeder.contains("role")) {
                seedRole();
                log.info("Success run role seeder");
            } else if(seeder.contains("activitysector")) {
                seedActivitySector();
                log.info("Success run activitySector seeder");
            } else if(seeder.contains("company")) {
                seedCompany();
                log.info("Success run Company seeder");
            } else if(seeder.contains("all")) {
                seedRole();
                log.info("Success run role seeder");
                seedActivitySector();
                log.info("Success run activitySector seeder");
                seedTypeOffre();
                log.info("Success run typeOffre seeder");
                seedNiveauExperience();
                log.info("Success run NiveauExperience seeder");
                seedNiveauScolaire();
                log.info("Success run NiveauScolaire seeder");
                seedCompany();
                log.info("Success run Company seeder");
                log.info("Success run all seeders");
            }
        }else{
            log.info("No seeder specified");
        }
    }


    private void seedActivitySector() throws Exception {
        List<String> sectors = new ArrayList<>();
        List<ActivitySector> allExistingSector = activitySectorService.getAll();

        if(allExistingSector.size() <= 0) {
            sectors.add("informatique, reseaux et developpement");
            sectors.add("economie et finance");
            sectors.add("agriculture");
            sectors.add("elevage");
            sectors.add("sport");
            sectors.add("commerce");
            sectors.add("education");
            sectors.add("transport");
            sectors.add("autres");

            var index = 0;
            for (var s : sectors ) {
                ActivitySector newSector = new ActivitySector();
                newSector.setLibelle(s);
                activitySectorService.create(newSector);
                log.info("Success run RoleSeeder {}",sectors.get(index));
                index++;
            }
        } else {
            log.info("Table activity_sector already contains data");
        }
    }

    private void seedRole() throws Exception {
        List<String> roles = new ArrayList<>();
        List<Role> allExistingRole = roleService.getAll();

        if(allExistingRole.size() <= 0) {
            roles.add("admin");
            roles.add("candidat");
            roles.add("recruteur");

            var index = 0;
            for (var role : roles ) {
                Role newRole = new Role();
                newRole.setLibelle(role);
                roleService.create(newRole);
                log.info("Success run RoleSeeder {}",roles.get(index));
                index++;
            }
        } else {
            log.info("Table activity_sector already contains data");
        }
    }

    private void seedTypeOffre() throws Exception {
        List<String> types = new ArrayList<>();
        List<TypeOffre> allExistingTypeOffre = typeOffreService.getAll();

        if(allExistingTypeOffre.size() <= 0) {
            types.add("stage");
            types.add("cdi");
            types.add("cdd");
            types.add("remote");

            var index = 0;
            for (var t : types ) {
                TypeOffre newType = new TypeOffre();
                newType.setLibelle(t);
                typeOffreService.create(newType);
                log.info("Success run TypeOffreSeeder {}",types.get(index));
                index++;
            }
        } else {
            log.info("Table type_offre already contains data");
        }
    }

    private void seedNiveauScolaire() throws Exception {
        List<String> levels = new ArrayList<>();
        List<NiveauScolaire> allExistingLevel = niveauScolaireService.getAll();

        if(allExistingLevel.size() <= 0) {
            levels.add("bepc");
            levels.add("bac");
            levels.add("bts");
            levels.add("licence");
            levels.add("master");
            levels.add("doctorat");

            var index = 0;
            for (var l : levels ) {
                NiveauScolaire newLevel = new NiveauScolaire();
                newLevel.setLibelle(l);
                niveauScolaireService.create(newLevel);
                log.info("Success run NiveauScolaireSeeder {}",levels.get(index));
                index++;
            }
        } else {
            log.info("Table niveau_scolaire already contains data");
        }
    }

    private void seedNiveauExperience() throws Exception {
        List<String> levels = new ArrayList<>();
        List<NiveauExperience> allExistingLevel = niveauExperienceService.getAll();

        if(allExistingLevel.size() <= 0) {
            levels.add("Aucun");
            levels.add("1 - 3 ans");
            levels.add("4 - 5 ans");
            levels.add("+5 ans");

            var index = 0;
            for (var l : levels ) {
                NiveauExperience newLevel = new NiveauExperience();
                newLevel.setLibelle(l);
                niveauExperienceService.create(newLevel);
                log.info("Success run NiveauExperienceSeeder {}",levels.get(index));
                index++;
            }
        } else {
            log.info("Table niveau_experience already contains data");
        }
    }

    private void seedCompany() throws Exception {
       try{
           List<String> companies = new ArrayList<>();
           List<Company> allExistingCompany = companyService.getAll();
           if(allExistingCompany.size() <= 0) {
               Company newCompany = new Company();
               ActivitySector sector = activitySectorService.getOneByLibelle("informatique, reseaux et developpement".toLowerCase());
               if(sector != null) {
                   //1
                   newCompany.setName("orange");
                   newCompany.setWebsitelink("https://www.orange.ci/");
                   companyService.create(newCompany);
                   // 2
                   newCompany.setName("mtn");
                   newCompany.setWebsitelink("https://www.mtn.ci/");
                   companyService.create(newCompany);
                   // 3
                   newCompany.setName("moov");
                   newCompany.setWebsitelink("https://www.moov-africa.ci/");
                   companyService.create(newCompany);
                   // 4
                   newCompany.setName("wave");
                   newCompany.setWebsitelink("https://www.wave.com/");
                   companyService.create(newCompany);
               }
               sector = activitySectorService.getOneByLibelle("economie et finance".toLowerCase());
               if(sector != null) {
                   // 1
                   newCompany.setName("sgbci");
                   newCompany.setWebsitelink("https://societegenerale.ci/fr/");
                   companyService.create(newCompany);
                   // 2
                   newCompany.setName("nsia");
                   newCompany.setWebsitelink("https://www.nsiabanque.ci/");
                   companyService.create(newCompany);
                   // 3
                   newCompany.setName("ecobank");
                   newCompany.setWebsitelink("https://www.ecobank.com/ci/personal-banking");
                   companyService.create(newCompany);
                   // 4
                   newCompany.setName("boa");
                   newCompany.setWebsitelink("https://boacoteivoire.com/");
                   companyService.create(newCompany);
               }

           } else {
               log.info("Table company already contains data");
           }
       } catch (Exception e) {
           log.error(e.toString());
           throw new Exception(e.toString());
       }
    }
}
