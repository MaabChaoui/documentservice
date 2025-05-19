// src/main/java/com/example/document/dto/AdminAnalyticsDto.java
package com.example.document.dto;

import java.time.LocalDateTime;
import java.util.Map;

import lombok.Data;

@Data
public class AdminAnalyticsDto {
    private long totalUsers;
    private long totalDocuments;

    // how many documents per category name
    private Map<String,Long> docsPerCategory;

    // how many documents per department name
    private Map<String,Long> docsPerDepartment;

    // how many documents in each status
    private Map<String,Long> docsByStatus;

    // how many new users / new docs in the last 7 days
    private long newUsersLast7Days;
    private long newDocsLast7Days;

    // when the snapshot was taken
    private LocalDateTime generatedAt = LocalDateTime.now();

    // getters + setters omitted for brevity
}
