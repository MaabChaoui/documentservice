package com.example.document.model;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

import org.hibernate.annotations.CreationTimestamp;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "departments")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Department {

    @Id
    @Column(name = "department_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "department_name")
    private String name;
    
    @CreationTimestamp                // <-- Hibernate sets this on INSERT
    @Column(name = "created_at",
            // nullable = false,
            updatable = false)        // never changes after insert
    private LocalDateTime createdAt;

    @Builder.Default
    @ManyToMany(mappedBy = "departments")
    @JsonIgnore
    private Set<User> users = new HashSet<>();

    // ✅ Don't include collections in equals/hashCode
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Department d)) return false;
        return id != null && id.equals(d.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
