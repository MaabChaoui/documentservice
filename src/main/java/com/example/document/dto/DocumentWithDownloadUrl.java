package com.example.document.dto;

import java.time.LocalDateTime;

import com.example.document.model.DocumentStatus;

import jakarta.persistence.Column;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
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

    @Enumerated(EnumType.STRING)
    private DocumentStatus status;

    private String downloadUrl;

    // public DocumentWithDownloadUrl(String id, String title, String category, String department, LocalDateTime creationDate, String createdBy, String status, String downloadUrl) {
    //     this.id = id;
    //     this.title = title;
    //     this.category = category;
    //     this.department = department;
    //     this.creationDate = creationDate;
    //     this.createdBy = createdBy;
    //     this.status = status;
    //     this.downloadUrl = downloadUrl;
    // }
    public DocumentWithDownloadUrl(String title, String category, String department, String downloadUrl) {
        this.title = title;
        this.category = category;
        this.department = department;
        this.downloadUrl = downloadUrl;
    }
}
