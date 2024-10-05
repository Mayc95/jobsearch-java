package com.maycdevs.jobsearch.model;

import jakarta.persistence.*;
import lombok.Data;
import org.springframework.boot.context.properties.bind.DefaultValue;

import java.io.Serializable;
import java.util.Date;
import java.util.Calendar;


@Data
@Entity
public class Offer implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(nullable = false, columnDefinition = "varchar(255) default ''")
    private String libelle;

    @Column(nullable = false, columnDefinition = "varchar(255) default ''")
    private String description;

    @Column(columnDefinition = "varchar(255) default ''")
    private String profil;

    @Column(nullable = false, columnDefinition = "varchar(255) default 'ENABLED'")
    private String etat="ENABLED";

    @Column(nullable = false, columnDefinition = "varchar(255) default 'ABIDJAN'")
    private String localisation="ABIDJAN";

    @Column(columnDefinition = "varchar(255) default ''")
    private String salaire="";

    @Column(columnDefinition = "varchar(255) default ''")
    private String langue="FRANCAIS";

    // represente l'adresse email a laquelle il faut ecrire pour postuler a l'offre.
    @Column(nullable = false, columnDefinition = "varchar(255) default ''")
    private String email="";

    // represente la date de creation de l'offre.
    @Column(name = "date_creation")
    private Date dateDeCreation = null;

    // represente la date de mise en ligne de l'offre.
    // doit toujours etre la meme valeur que dateDeCreation jusqu'a ce que
    // le fonctionnalite de "publication programmee" existe
    @Column(name = "date_publication")
    private Date dateDePublication = null;

    // represente la date d'expiration ou de fin de validite de l'offre.
    @Column(name = "date_expiration")
    private Date dateExpiration = null;

    @OneToOne(fetch = FetchType.EAGER, cascade = CascadeType.REMOVE)
    private Attachement image = null;
    private String imageLocation = null;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "niveau_scolaire_id")
    private NiveauScolaire niveauScolaire = null;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "niveau_experience_id")
    private NiveauExperience niveauExperience = null;

    // propriete de liaison avec la table "typeOffre",
    // represente le type de l'offre.
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "type_offre_id")
    private TypeOffre typeOffre = null;

    // propriete de liaison avec la table "ActivitySector",
    // represente le secteur d'activite lier a l'offre.
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "activity_sector_id")
    private ActivitySector activitySector = null;

    // propriete de liaison avec la table "company",
    // represente l'entreprise qui a emis l'offre.
    // cette propriete ne peut etre null

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "company_id")
    private Company company = null;
    @ManyToOne(fetch = FetchType.EAGER)
    private User author = null;

    // s'execute avant de sauvegarder les donnees dans la bdd a cause de @PrePersist
    @PrePersist
    void beforeInsertion() {
        if(this.dateDeCreation == null) {
            this.dateDeCreation = Calendar.getInstance().getTime();
        }
        if(this.dateDePublication == null) {
            this.dateDePublication = this.dateDeCreation;
        }
        if(this.dateExpiration == null) {
            this.dateExpiration = this.dateDeCreation;
        }
    }

}
