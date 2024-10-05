package com.maycdevs.jobsearch.repository;

import com.maycdevs.jobsearch.model.NiveauExperience;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface NiveauExperienceRepository extends JpaRepository<NiveauExperience, Long> {
}
