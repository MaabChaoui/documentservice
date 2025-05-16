package com.example.document.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.util.HashSet;
import java.util.Set;

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
