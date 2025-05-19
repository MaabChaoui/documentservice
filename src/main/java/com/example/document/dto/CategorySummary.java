// src/main/java/com/example/document/dto/CategorySummary.java
package com.example.document.dto;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonProperty;

public interface CategorySummary {

    Integer getId();

    String getName();

    LocalDateTime getCreatedAt();

    @JsonProperty("docs_count")
    Long getDocsCount();
}
