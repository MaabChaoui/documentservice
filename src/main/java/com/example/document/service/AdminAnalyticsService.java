// src/main/java/com/example/document/service/AdminAnalyticsService.java
package com.example.document.service;

import com.example.document.dto.AdminAnalyticsDto;
import com.example.document.repository.*;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.stream.Collectors;

@Service
public class AdminAnalyticsService {

    private final UserRepository       userRepo;
    private final DocumentRepository   docRepo;
    private final CategoryRepository   catRepo;
    private final DepartmentRepository deptRepo;

    public AdminAnalyticsService(UserRepository userRepo,
                                 DocumentRepository docRepo,
                                 CategoryRepository catRepo,
                                 DepartmentRepository deptRepo) {
        this.userRepo = userRepo;
        this.docRepo = docRepo;
        this.catRepo = catRepo;
        this.deptRepo = deptRepo;
    }

    public AdminAnalyticsDto gather() {
        AdminAnalyticsDto dto = new AdminAnalyticsDto();

        // totals
        dto.setTotalUsers(userRepo.count());
        dto.setTotalDocuments(docRepo.count());

        // docs per category
        dto.setDocsPerCategory(
            catRepo.findDocumentCountsPerCategory().stream()
                   .collect(Collectors.toMap(
                       m -> (String) m.get("name"),
                       m -> ((Number) m.get("cnt")).longValue()
                   ))
        );

        // docs per department
        dto.setDocsPerDepartment(
            deptRepo.findDocumentCountsPerDepartment().stream()
                   .collect(Collectors.toMap(
                       m -> (String) m.get("name"),
                       m -> ((Number) m.get("cnt")).longValue()
                   ))
        );

        // docs by status
        dto.setDocsByStatus(
            docRepo.findDocumentCountsByStatus().stream()
                   .collect(Collectors.toMap(
                       m -> m.get("status").toString(),
                       m -> ((Number) m.get("cnt")).longValue()
                   ))
        );

        // recent activity (last 7 days)
        LocalDateTime weekAgo = LocalDateTime.now().minusDays(7);
        dto.setNewUsersLast7Days(userRepo.countByCreatedAtAfter(weekAgo));
        dto.setNewDocsLast7Days(docRepo.countByCreatedAtAfter(weekAgo));

        return dto;
    }
}
