package com.example.document.dto;

import java.time.LocalDateTime;

import com.example.document.model.Document;

import lombok.Data;

@Data
public class DocumentResponse {
    private Integer id;
    private String title;
    private String department;
    private String category;
    private String fileUrl;
    private LocalDateTime createdAt;
    private String createdBy;
    private String status;
    private String type;
    private Long size;

    public DocumentResponse(Document doc) {
        this.id         = doc.getId();
        this.title      = doc.getTitle();
        this.department = doc.getDepartment().getName();
        this.category   = doc.getCategory().getName();
        this.fileUrl    = doc.getFileUrl();
        this.createdAt  = doc.getCreatedAt();
        this.createdBy  = doc.getCreatedBy();
        this.status     = doc.getStatus();
        this.type       = doc.getType();
        this.size       = doc.getSize();
    }
}
