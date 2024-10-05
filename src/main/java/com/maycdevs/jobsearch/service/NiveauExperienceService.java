package com.maycdevs.jobsearch.service;

import com.maycdevs.jobsearch.model.NiveauExperience;
import com.maycdevs.jobsearch.model.Role;
import com.maycdevs.jobsearch.repository.NiveauExperienceRepository;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Slf4j
@Service
public class NiveauExperienceService implements EntityCRUDService<NiveauExperience> {


    @Autowired
    private NiveauExperienceRepository niveauExperienceRepository;

    public List<NiveauExperience> getAll() {
        return niveauExperienceRepository.findAll();
    }

    public NiveauExperience getOneById(String idInString) throws Exception {
        try {
            Long id = Long.parseLong(idInString);
            return niveauExperienceRepository.findById(id).orElse(null);
        } catch (Exception e) {
            log.debug(e.toString());
            e.printStackTrace();
            throw e;
        }
    }

    public NiveauExperience create(@NonNull NiveauExperience data) throws Exception {
        // verification des valeurs des proprietes de l'objet level
        try {
            return  niveauExperienceRepository.save(data);
        } catch (Exception e) {
            log.debug(e.toString());
            e.printStackTrace();
            throw e;
        }
    }

    public NiveauExperience update(String id, NiveauExperience data) throws Exception {
        NiveauExperience current = getOneById(id);
        if(current != null) {
            try {
                String lib = data.getLibelle();
                if(lib != null) {
                    current.setLibelle(lib);
                }

                String desc = data.getDescription();
                if(desc != null) {
                    current.setDescription(desc);
                }

                Date datecreation = data.getDateDeCreation();
                if(datecreation != null) {
                    current.setDateDeCreation(datecreation);
                }

                return niveauExperienceRepository.save(current);
            } catch (Exception e) {
                log.debug(e.toString());
                e.printStackTrace();
                throw e;
            }
        }

        return null;
    }

    public Boolean delete(String id) throws Exception {
        NiveauExperience level = getOneById(id);
        if(level != null) {
            try {
                niveauExperienceRepository.delete(level);
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
