package com.example.document.dto;

import lombok.Data;

@Data
public class DocumentMetadataRequest {

    private String title;
    private String status;
    
    private String category;
    private String department;
}
