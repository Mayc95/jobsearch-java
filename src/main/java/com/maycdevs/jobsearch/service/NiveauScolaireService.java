package com.maycdevs.jobsearch.service;

import com.maycdevs.jobsearch.model.NiveauExperience;
import com.maycdevs.jobsearch.model.NiveauScolaire;
import com.maycdevs.jobsearch.repository.NiveauScolaireRepository;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Slf4j
@Service
public class NiveauScolaireService implements EntityCRUDService<NiveauScolaire> {

    @Autowired
    private NiveauScolaireRepository niveauScolaireRepository;

    public List<NiveauScolaire> getAll() {
        return niveauScolaireRepository.findAll();
    }

    public NiveauScolaire getOneById(String idInString) throws Exception {
        try {
            Long id = Long.parseLong(idInString);
            return niveauScolaireRepository.findById(id).orElse(null);
        } catch (Exception e) {
            log.debug(e.toString());
            e.printStackTrace();
            throw e;
        }
    }

    public NiveauScolaire create(@NonNull NiveauScolaire data) throws Exception {
        // verification des valeurs des proprietes de l'objet niveau
        try {
            return niveauScolaireRepository.save(data);
        } catch (Exception e) {
            log.debug(e.toString());
            e.printStackTrace();
            throw e;
        }
    }

    public NiveauScolaire update(String id, NiveauScolaire data) throws Exception {
        NiveauScolaire current = getOneById(id);
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

                return niveauScolaireRepository.save(current);
            } catch (Exception e) {
                log.debug(e.toString());
                e.printStackTrace();
                throw e;
            }
        }

        return null;
    }

    public Boolean delete(String id) throws Exception {
        NiveauScolaire niveau = getOneById(id);
        if(niveau != null) {
            try {
                niveauScolaireRepository.delete(niveau);
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
