package com.example.document.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "documents")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Document {

    @Id
    @Column(name = "document_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String title;

    @ManyToOne
    @JoinColumn(name = "department_id")
    @JsonIgnoreProperties("users") // ✅ prevent recursive Department → users → Document → ...
    private Department department;

    @ManyToOne
    @JoinColumn(name = "category_id")
    private Category category;

    @Column(name = "file_url")
    private String fileUrl;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    private String createdBy;

    @Enumerated(EnumType.STRING)
    private DocumentStatus status;
}
