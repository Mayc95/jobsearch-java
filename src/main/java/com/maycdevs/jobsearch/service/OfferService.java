package com.maycdevs.jobsearch.service;

import com.maycdevs.jobsearch.model.*;
import com.maycdevs.jobsearch.repository.OfferRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

@Slf4j
@Service
public class OfferService implements EntityCRUDService<Offer> {

    @Autowired
    private OfferRepository offerRepository;
    @Autowired
    private ActivitySectorService activitySectorService;
    @Autowired
    private NiveauScolaireService niveauScolaireService;
    @Autowired
    private NiveauExperienceService niveauExperienceService;
    @Autowired
    private TypeOffreService typeOffreService;
    @Autowired
    private CompanyService companyService;

    @Override
    public List<Offer> getAll() {
        return offerRepository.findAll();
    }

    @Override
    public Offer getOneById(String idInString) throws Exception {
        try {
            Long id = Long.parseLong(idInString);
            return offerRepository.findById(id).orElse(null);
        } catch (Exception e) {
            log.debug(e.toString());
            e.printStackTrace();
            throw new Exception("Could not find Offer with id: "+idInString+" in database because: "+e.toString());
        }
    }

    @Override
    public Offer create(Offer data) throws Exception {
        // verification des valeurs des proprietes de l'objet level
        if(
                data.getNiveauExperience() != null &&
                data.getNiveauScolaire() != null &&
                data.getActivitySector() != null &&
                data.getTypeOffre() != null &&
                data.getCompany() != null
        ) {
            try {
                return offerRepository.save(data);
            } catch (Exception e) {
                log.debug(e.toString());
                e.printStackTrace();
                throw new Exception("Could not create Offer with libelle: "+data.getLibelle()+" in database because: "+e.toString());
            }
        }

        return null;
    }

    @Override
    public Offer update(String id, Offer data) throws Exception {
        Offer current = getOneById(id);
        if(current != null) {

            String libelle = data.getLibelle();
            if(!libelle.isBlank()) {
                current.setLibelle(libelle);
            }

            String description = data.getDescription();
            if(!description.isBlank()) {
                current.setDescription(description);
            }

            String profil = data.getProfil();
            if(profil != null && !profil.isBlank()) {
                current.setProfil(profil);
            }

            String localisation = data.getLocalisation();
            if(!localisation.isBlank()) {
                current.setLocalisation(localisation.toUpperCase());
            }

            String salaire = data.getSalaire();
            if(!salaire.isBlank()) {
                current.setSalaire(salaire);
            }

            String langue = data.getLangue();
            if(!langue.isBlank()) {
                current.setLangue(langue.toUpperCase());
            }

            String email = data.getEmail();
            if(!email.isBlank()) {
                current.setEmail(email);
            }

            Date datePublication = data.getDateDePublication();
            if(datePublication != null) {
                current.setDateDePublication(datePublication);
            }

            Date dateExpiration = data.getDateExpiration();
            if(dateExpiration != null) {
                current.setDateExpiration(dateExpiration);
            }

            Attachement newImage = data.getImage();
            if(newImage != null) {
                current.setImage(newImage);
            }
            String newImageLocation = data.getImageLocation();
            if(newImageLocation != null) {
                current.setImageLocation(newImageLocation);
            }


            if(data.getNiveauExperience() != null) {
                NiveauExperience levelExp = niveauExperienceService.getOneById(data.getNiveauExperience().getId().toString());
                if(levelExp != null ) {
                    current.setNiveauExperience(levelExp);
                }
            }

            if(data.getNiveauScolaire() != null) {
                NiveauScolaire level = niveauScolaireService.getOneById(data.getNiveauScolaire().getId().toString());
                if(level != null ) {
                    current.setNiveauScolaire(level);
                }
            }

            if(data.getTypeOffre() != null) {
                TypeOffre typeoffre = typeOffreService.getOneById(data.getTypeOffre().getId().toString());
                if(typeoffre != null ) {
                    current.setTypeOffre(typeoffre);
                }
            }

            if(data.getActivitySector() != null) {
                ActivitySector sector = activitySectorService.getOneById(data.getActivitySector().getId().toString());
                if(sector != null ) {
                    current.setActivitySector(sector);
                }
            }

            if(data.getCompany() != null) {
                Company company = companyService.getOneById(data.getCompany().getId().toString());
                if(company != null ) {
                    current.setCompany(company);
                }
            }

            try {
                return offerRepository.save(current);
            } catch (Exception e) {
                log.debug(e.toString());
                e.printStackTrace();
                throw new Exception("Could not update Offer with libelle: "+data.getLibelle()+" in database because: "+e.toString());
            }
        }
        return null;
    }

    @Override
    public Boolean delete(String id) throws Exception {
        Offer offre = getOneById(id);
        if(offre != null) {
            try {
                offerRepository.delete(offre);
                return true;
            } catch (Exception e) {
                log.debug(e.toString());
                e.printStackTrace();
                throw new Exception("Could not delete Offer with id: "+id+" in database because: "+e.toString());
            }
        }
        return false;
    }

    public List<Offer> getAllByActivitySectorLibelle(String libelle) {
        List<Offer> data = new ArrayList<Offer>();
        offerRepository.findAll().forEach((offer -> {
            ActivitySector activitySectorOffer = offer.getActivitySector();
            if (activitySectorOffer.getLibelle().equalsIgnoreCase(libelle)) {
                data.add(offer);
            }
        }));

        return data;
    }

    public int countAllByActivitySectorLibelle(String libelle) {
        return getAllByActivitySectorLibelle(libelle).size();
    }
}
