package com.maycdevs.jobsearch.model;

import jakarta.persistence.*;
import lombok.Data;

import java.io.Serializable;
import java.util.Set;

@Data
@Entity
@Table(name = "activity_sector")
public class ActivitySector implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(nullable = false, columnDefinition = "varchar(255) default ''")
    private String libelle;

    @Column(columnDefinition = "varchar(255) default ''")
    private String description;


    @OneToMany
    private Set<Offer> allOffers = null;

}
