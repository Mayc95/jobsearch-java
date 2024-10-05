package com.maycdevs.jobsearch.service;

import com.maycdevs.jobsearch.model.ActivitySector;
import com.maycdevs.jobsearch.repository.ActivitySectorRepository;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
public class ActivitySectorService implements EntityCRUDService<ActivitySector> {

    @Autowired
    private ActivitySectorRepository activitySectorRepository;

    public List<ActivitySector> getAll() {
        return activitySectorRepository.findAll();
    }

    public ActivitySector getOneById(String idInString) throws Exception {
        try {
            Long id = Long.parseLong(idInString);
            return activitySectorRepository.findById(id).orElse(null);
        } catch (Exception e) {
            log.debug(e.toString());
            e.printStackTrace();
            throw e;
        }
    }

    public Boolean delete(String id) throws Exception {
        ActivitySector sector = this.getOneById(id);
        if(sector != null) {
            try {
                activitySectorRepository.delete(sector);
                return true;
            } catch (Exception e) {
                log.debug(e.toString());
                e.printStackTrace();
                throw e;
            }
        }
        return false;
    }

    public ActivitySector create(@NonNull ActivitySector data) throws Exception {
        try {
            return activitySectorRepository.save(data);
        } catch (Exception e) {
            log.debug(e.toString());
            e.printStackTrace();
            throw e;
        }
    }

    public ActivitySector update(String id, @NonNull ActivitySector data) throws Exception {
        ActivitySector currentSector = this.getOneById(id);
        if(currentSector != null) {

            String newLibelle = data.getLibelle();
            if(newLibelle != null) {
                currentSector.setLibelle(newLibelle);
            }

            String newDescription = data.getDescription();
            if(newDescription != null) {
                currentSector.setDescription(newDescription);
            }

            try {
                return activitySectorRepository.save(currentSector);
            } catch (Exception e) {
                log.debug("Erreur pendant modification objet ActivitySector via service ActivitySectorService");
                log.debug(e.toString());
                e.printStackTrace();
                throw e;
            }
        }
        return null;
    }

    public ActivitySector getOneByLibelle(String libelle) throws Exception {
        return activitySectorRepository.findByLibelle(libelle).orElseThrow(
                () -> new Exception("Activity sector not found with libelle: "+ libelle)
        );
    }

}
