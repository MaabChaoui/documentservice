// src/main/java/com/example/document/dto/DepartmentSummary.java
package com.example.document.dto;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonProperty;

/** Shape of the JSON we’ll send back. */
public interface DepartmentSummary {

    Long getId();
    String getName();
    LocalDateTime getCreatedAt();

    @JsonProperty("users_count")     // ➜  "users_count": 5
    Long getUsersCount();
}
