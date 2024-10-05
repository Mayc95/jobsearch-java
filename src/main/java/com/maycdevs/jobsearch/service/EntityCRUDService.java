package com.maycdevs.jobsearch.service;

import java.util.List;

public interface EntityCRUDService<T> {

    public List<T> getAll();

    public T getOneById(String idInString) throws Exception;

    public T create(T data) throws Exception;

    public T update(String id, T data) throws Exception;

    public Boolean delete(String id) throws Exception;
}
