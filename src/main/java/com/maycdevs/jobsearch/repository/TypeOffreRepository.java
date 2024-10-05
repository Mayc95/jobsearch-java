package com.maycdevs.jobsearch.repository;

import com.maycdevs.jobsearch.model.TypeOffre;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TypeOffreRepository extends JpaRepository<TypeOffre, Long> {
}
