package com.example.document.dto;

import lombok.Data;

@Data
public class DocumentRequest {
    private String title;
    private Long categoryId;
    private Long departmentId;
    private String fileUrl;
    private String status;
    private String type;
    private Long size;
}
