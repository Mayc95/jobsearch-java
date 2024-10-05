package com.maycdevs.jobsearch.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;

import java.io.Serializable;

@Data
@Entity
@NoArgsConstructor
public class Attachement implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", nullable = false)
    private Long id;

    private String key = "";

    private String filename;

    private String filetype;

    private String filelocation;

    private String filedownloadurl="";

    @Lob
    private byte[] filedata;

    public Attachement(String filename, String key, String filetype, String filelocation, byte[] filedata) {
        this.filename = filename;
        this.key = key;
        this.filetype = filetype;
        this.filelocation = filelocation;
        this.filedata = filedata;
    }

}
