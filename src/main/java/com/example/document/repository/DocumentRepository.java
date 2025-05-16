package com.example.document.repository;

import com.example.document.model.Document;
import com.example.document.model.Department;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface DocumentRepository extends JpaRepository<Document, Long> {
    List<Document> findByDepartmentIn(List<Department> departments);
}
