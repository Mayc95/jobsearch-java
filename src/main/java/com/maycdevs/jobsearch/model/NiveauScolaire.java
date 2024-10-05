package com.maycdevs.jobsearch.model;

import jakarta.persistence.*;
import lombok.Data;

import java.util.Calendar;
import java.util.Date;

@Data
@Entity
public class NiveauScolaire {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(nullable = false, columnDefinition = "varchar(255) default ''")
    private String libelle;

    @Column(columnDefinition = "varchar(255) default ''")
    private String description;

    @Column(name = "date_creation")
    private Date dateDeCreation = null;

    @PrePersist
    public void beforeInsert() {
        if(this.dateDeCreation == null) {
            this.dateDeCreation = Calendar.getInstance().getTime();
        }
    }
}
