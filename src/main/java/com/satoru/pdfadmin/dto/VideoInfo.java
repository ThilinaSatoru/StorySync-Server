package com.satoru.pdfadmin.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Date;

@Data
@AllArgsConstructor
public class VideoInfo {
    private String name;
    private boolean directory;
    private long size;
    private Date lastModified;
}
