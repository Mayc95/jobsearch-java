package com.maycdevs.jobsearch.service;

import com.maycdevs.jobsearch.model.Attachement;
import org.springframework.web.multipart.MultipartFile;

public interface ManageFileService {
    Attachement storeInDB(MultipartFile file) throws Exception;

    Attachement getOneById(String idInString) throws Exception;

    Attachement getOneByKey(String key) throws Exception;

    String createDownloadUrl(Attachement file);

    Boolean deleteById(String id) throws Exception;
}
