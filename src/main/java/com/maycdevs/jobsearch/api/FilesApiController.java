package com.maycdevs.jobsearch.api;

import com.maycdevs.jobsearch.model.Attachement;
import com.maycdevs.jobsearch.service.FileStorageService;
import com.maycdevs.jobsearch.service.ManageFileService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping(value = "/api/v1/files")
public class FilesApiController {

    @Autowired
    private ManageFileService manageFileService;

    @Autowired
    private FileStorageService fileStorageService;


    @Transactional(readOnly = true)
    @GetMapping("/download/{key}")
    public ResponseEntity<Resource> downloadAttachement(@PathVariable("key") String key) throws Exception {
        Attachement fichier = manageFileService.getOneByKey(key);
        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(fichier.getFiletype()))
                .header(HttpHeaders.CONTENT_DISPOSITION, "attchement; filename=\""+fichier.getFilename()+"\"")
                .body(new ByteArrayResource(fichier.getFiledata()));
    }

    @Transactional(readOnly = true)
    @GetMapping("/display/{key}")
    public ResponseEntity<byte[]> displayAttachement(@PathVariable("key") String key) throws Exception {
        Attachement fichier = manageFileService.getOneByKey(key);
        return ResponseEntity.ok()
                .contentType(MediaType.IMAGE_PNG)
                .body(fichier.getFiledata());
    }

    @GetMapping("/show/{filename}")
    public ResponseEntity<Resource> displayFile(@PathVariable("filename") String filename) throws Exception {
        Resource file = fileStorageService.loadAsResource(filename);

        if (file == null)
            return ResponseEntity.notFound().build();

        return ResponseEntity.ok().header(HttpHeaders.CONTENT_DISPOSITION,
                "attachment; filename=\"" + file.getFilename() + "\"").body(file);
    }



}
