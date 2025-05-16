package com.example.document.dto;

public class DocumentMetadataRequest {
    
    private String title;
    private String status;  // Optional, based on your use case

    // Getters and Setters
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
