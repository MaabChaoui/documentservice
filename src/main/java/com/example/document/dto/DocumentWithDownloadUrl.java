package com.example.document.dto;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class DocumentWithDownloadUrl {

    private Integer id;
    private String title;
    private String department;
    private String category;
    private LocalDateTime creationDate;
    private String createdBy;

    private String status;
    
    private String type;
    private Long size;

    private String downloadUrl;

    public DocumentWithDownloadUrl(String title, String category, String department, String downloadUrl) {
        this.title = title;
        this.category = category;
        this.department = department;
        this.downloadUrl = downloadUrl;
    }
}
