package com.maycdevs.jobsearch.model;

import jakarta.persistence.*;
import lombok.Data;

import java.io.Serializable;
import java.util.Calendar;
import java.util.Date;

@Data
@Entity
public class Company implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(nullable = false, columnDefinition = "varchar(255) default ''")
    private String name;

    @Column(columnDefinition = "varchar(255) default ''")
    private String description;

    @Column(name = "website_link", columnDefinition = "varchar(255) default ''")
    private String websitelink="";
    @Column(name = "date_creation")
    private Date dateDeCreation = null;

    @OneToOne(fetch = FetchType.EAGER, cascade = CascadeType.REMOVE)
    private Attachement logo=null;
    private String logoLocation=null;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "activity_sector_id", nullable = false)
    private ActivitySector activitySector;

    // s'execute avant de sauvegarder les donnees dans la bdd a cause de @PrePersist
    @PrePersist
    void beforeInsertion() {
        if(this.dateDeCreation == null) {
            this.dateDeCreation = Calendar.getInstance().getTime();
        }
    }
}
