package com.maycdevs.jobsearch.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
public class EntityCRUDServiceImpl implements EntityCRUDService<Class>{


    private String entityName = "";
    private JpaRepository<Class, Long> repository;

    public EntityCRUDServiceImpl(JpaRepository<Class, Long> repository) {
        this.repository = repository;
    }


    @Override
    public List<Class> getAll() {
        return repository.findAll();
    }

    @Override
    public Class getOneById(String idInString) throws Exception {
        try {
            Long id = Long.parseLong(idInString);
            return repository.findById(id).orElse(null);
        } catch (Exception e) {
            log.debug(e.toString());
            e.printStackTrace();
            throw new Exception("Could not find "+entityName+" with id: "+idInString+" in database because: "+e.toString());
        }
    }

    @Override
    public Class create(Class data) throws Exception {
        return null;
    }

    @Override
    public Class update(String id, Class data) throws Exception {
        return null;
    }

    @Override
    public Boolean delete(String id) throws Exception {
        Class entity = getOneById(id);
        if (entity != null) {
           try {
               repository.delete(entity);
               return true;
           } catch (Exception e) {
               log.debug(e.toString());
               e.printStackTrace();
               throw new Exception("Could not delete "+entityName+" with id: "+id+" in database because: "+e.toString());
           }
        }
        return null;
    }
}
