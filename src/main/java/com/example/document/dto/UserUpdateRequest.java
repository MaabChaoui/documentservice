package com.example.document.dto;

import lombok.Data;

@Data
public class UserUpdateRequest {
    private String email;
    private String name;
    private String role;
    private String status;
}
