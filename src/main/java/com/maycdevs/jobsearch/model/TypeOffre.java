package com.maycdevs.jobsearch.model;

import jakarta.persistence.*;
import lombok.Data;

import java.io.Serializable;

@Data
@Entity
public class TypeOffre implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(nullable = false, columnDefinition = "varchar(255) default ''")
    private String libelle;

    @Column(columnDefinition = "varchar(255) default ''")
    private String description;
}
