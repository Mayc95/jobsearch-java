package com.maycdevs.jobsearch.model;

import jakarta.persistence.*;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;
import java.util.Calendar;

@Data
@Entity
@Table(name = "user_role")
public class Role implements Serializable {
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

    // s'execute avant de sauvegarder les donnees dans la bdd a cause de @PrePersist
    @PrePersist
    void beforeInsertion() {
        if(this.dateDeCreation == null) {
            this.dateDeCreation = Calendar.getInstance().getTime();
        }
    }
}
