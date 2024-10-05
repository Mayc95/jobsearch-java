package com.maycdevs.jobsearch.repository;

import com.maycdevs.jobsearch.model.Attachement;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AttachementRepository extends JpaRepository<Attachement, String> {

    Optional<Attachement> findByKey(String key);

}
