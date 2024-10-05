package com.maycdevs.jobsearch.service;

import com.maycdevs.jobsearch.model.Attachement;
import com.maycdevs.jobsearch.repository.AttachementRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.util.Objects;
import java.util.UUID;

@Slf4j
@Service
public class AttachementService implements ManageFileService {

    @Autowired
    private AttachementRepository attachementRepository;

    @Override
    public Attachement storeInDB(MultipartFile file) throws Exception {
        String fileName = StringUtils.cleanPath(Objects.requireNonNull(file.getOriginalFilename()));
        try {
            if(!fileName.isBlank() && fileName.contains("..")) {
                throw new Exception("File's name contains invalid sequence : "+fileName);
            }

            String downloadKey = UUID.randomUUID().toString();

            Attachement newFile = new Attachement(fileName, downloadKey, file.getContentType(), "", file.getBytes());
            newFile.setFiledownloadurl(createDownloadUrl(newFile));

            return attachementRepository.save(newFile);
        } catch (Exception e) {
            log.debug(e.toString());
            e.printStackTrace();
            throw new Exception("Could not save file "+fileName+" in database because: "+e.toString());

        }
    }

    @Override
    public Attachement getOneById(String idInString) throws Exception {
        return attachementRepository.findById(idInString).orElseThrow(
                () -> new Exception("File not found with ID: "+ idInString)
        );
    }

    @Override
    public Attachement getOneByKey(String key) throws Exception {
        return attachementRepository.findByKey(key).orElseThrow(
                () -> new Exception("File not found with Key: "+ key)
        );
    }

    @Override
    public String createDownloadUrl(Attachement file) {
        String url = "";

        url = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path("api/v1/files/download/")
                .path(file.getKey())
                .toUriString();

        return url;
    }

    @Override
    public Boolean deleteById(String id) throws Exception {
        Attachement fileToDelete = getOneById(id);
        try {
            attachementRepository.delete(fileToDelete);
            return true;
        } catch (Exception e) {
            log.debug(e.toString());
            e.printStackTrace();
            throw new Exception("Could not delete file "+fileToDelete.getFilename()+" in database because: "+e.toString());
        }
    }
}
