package com.maycdevs.jobsearch.service;

import com.maycdevs.jobsearch.model.Role;
import com.maycdevs.jobsearch.repository.RoleRepository;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Slf4j
@Service
public class RoleService implements EntityCRUDService<Role> {

    @Autowired
    private RoleRepository roleRepository;

    public List<Role> getAll() {
        return roleRepository.findAll();
    }

    public Role getOneById(String idInString) throws Exception {
        try {
            Long id = Long.parseLong(idInString);
            return roleRepository.findById(id).orElse(null);
        } catch (Exception e) {
            log.debug(e.toString());
            e.printStackTrace();
            throw new Exception("Could not find Role with id: "+idInString+" in database because: "+e.toString());
        }
    }

    public Role getOneByLibelle(String libelle) throws Exception {
        try {
            return roleRepository.findByLibelle(libelle).orElse(null);
        } catch (Exception e) {
            log.debug(e.toString());
            e.printStackTrace();
            throw new Exception("Could not find Role with libelle: "+libelle+" in database because: "+e.toString());
        }
    }

    public Role create(@NonNull Role data) throws Exception {
        // faire les controls sur les valeurs des proprietes de "r"
        try {
            return roleRepository.save(data);
        } catch (Exception e) {
            log.debug(e.toString());
            e.printStackTrace();
            throw e;
        }
    }

    public Role update(String id, Role data) throws Exception {
        Role current = getOneById(id);
        if (current != null) {
            try {
                String lib = data.getLibelle();
                if(lib != null) {
                    current.setLibelle(lib);
                }

                String desc = data.getDescription();
                if(desc != null) {
                    current.setDescription(desc);
                }

                Date datecreation = data.getDateDeCreation();
                if(datecreation != null) {
                    current.setDateDeCreation(datecreation);
                }

                current = roleRepository.save(current);
            } catch (Exception e) {
                log.debug(e.toString());
                e.printStackTrace();
                throw e;
            }
        }

        return current;
    }

    public Boolean delete(String id) throws Exception {
        Role r = getOneById(id);
        if(r != null) {
            try {
                roleRepository.delete(r);
                return true;
            } catch (Exception e) {
                log.debug(e.toString());
                e.printStackTrace();
                throw e;
            }

        }
        return false;
    }

}
