package com.maycdevs.jobsearch.repository;

import com.maycdevs.jobsearch.model.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {

    public Optional<Role> findByLibelle(String libelle);

}
