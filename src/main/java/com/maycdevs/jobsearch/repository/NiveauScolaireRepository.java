package com.maycdevs.jobsearch.repository;

import com.maycdevs.jobsearch.model.NiveauScolaire;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface NiveauScolaireRepository extends JpaRepository<NiveauScolaire, Long> {
}
