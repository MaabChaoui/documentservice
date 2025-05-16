package com.example.document.dto;

public class DocumentWithDownloadUrl {

    private String title;
    private String category;
    private String department;
    private String downloadUrl;

    public DocumentWithDownloadUrl(String title, String category, String department, String downloadUrl) {
        this.title = title;
        this.category = category;
        this.department = department;
        this.downloadUrl = downloadUrl;
    }

    // Getters and Setters
    public String getTitle() {
        return title;
    }

    public String getCategory() {
        return category;
    }

    public String getDepartment() {
        return department;
    }

    public String getDownloadUrl() {
        return downloadUrl;
    }
}
