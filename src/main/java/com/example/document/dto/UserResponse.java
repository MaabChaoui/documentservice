package com.example.document.dto;

import com.example.document.model.User;
import lombok.Data;

import java.time.LocalDateTime;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Data
public class UserResponse {

    private Long id;
    private String email;
    private String name;
    private String role;
    private String status;
    private LocalDateTime created_at;

    private List<Long> departmentIds;

    public UserResponse(User user) {
        this.id = user.getId();
        this.email = user.getEmail();
        this.name = user.getName();
        this.role = user.getRole();
        this.status = user.getStatus();
        this.created_at = user.getCreatedAt();
        this.departmentIds = user.getDepartments() == null
                ? Collections.emptyList()
                : user.getDepartments().stream()
                        .map(d -> d.getId())
                        .collect(Collectors.toList());
    }
}
