package com.maycdevs.jobsearch.service;


import com.maycdevs.jobsearch.model.TypeOffre;
import com.maycdevs.jobsearch.repository.TypeOffreRepository;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
public class TypeOffreService implements EntityCRUDService<TypeOffre> {

    @Autowired
    private TypeOffreRepository typeOffreRepository;

    public List<TypeOffre> getAll() { return typeOffreRepository.findAll(); }

    public TypeOffre getOneById(String idInString) throws Exception {
        try {
            Long id = Long.parseLong(idInString);
            return typeOffreRepository.findById(id).orElse(null);
        } catch (Exception e) {
            log.debug(e.toString());
            e.printStackTrace();
            throw e;
        }
    }

    public Boolean delete(String id) throws Exception {
        TypeOffre t = this.getOneById(id);
        if(id != null) {
            try {
                typeOffreRepository.delete(t);
                return true;
            } catch (Exception e) {
                log.debug(e.toString());
                e.printStackTrace();
                throw e;
            }

        }
        return false;
    }

    public TypeOffre create(@NonNull TypeOffre data) throws Exception {
        try {
            return typeOffreRepository.save(data);
        } catch (Exception e) {
            log.debug(e.toString());
            e.printStackTrace();
            throw e;
        }
    }

    public TypeOffre update(String id, @NonNull TypeOffre data) throws Exception {
        TypeOffre currentTypeOffre = this.getOneById(id);
        if(currentTypeOffre != null) {
            String libelle = data.getLibelle();
            if(libelle != null) {
                currentTypeOffre.setLibelle(libelle);
            }

            String description = data.getDescription();
            if(description != null) {
                currentTypeOffre.setDescription(description);
            }

            try {
                return typeOffreRepository.save(currentTypeOffre);
            } catch (Exception e) {
                log.debug(e.toString());
                e.printStackTrace();
                throw e;
            }
        }
        return null;
    }
}
