package com.maycdevs.jobsearch.repository;

import com.maycdevs.jobsearch.model.ActivitySector;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ActivitySectorRepository extends JpaRepository<ActivitySector, Long> {

    public Optional<ActivitySector> findByLibelle(String libelle);
}
