package com.example.document.repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.document.model.Department;
import com.example.document.model.Document;

public interface DocumentRepository extends JpaRepository<Document, Long> {

    // obsolete
    List<Document> findByDepartmentIn(List<Department> departments);

    @Query("""
        SELECT d
        FROM   Document d
        JOIN   d.department dept
        JOIN   dept.users u
        WHERE  u.email = :email
    """)
    List<Document> findAllVisibleToUserByEmail(@Param("email") String email);

    @Query("""
   SELECT d.status  AS status,
          COUNT(d.id) AS cnt
   FROM   Document d
   GROUP  BY d.status
""")
    List<Map<String, Object>> findDocumentCountsByStatus();

    long count();

    long countByCreatedAtAfter(LocalDateTime cutoff);

    Page<Document> findByTitleContainingIgnoreCaseOrDepartment_NameContainingIgnoreCaseOrCategory_NameContainingIgnoreCase(
        String title,
        String deptName,
        String catName,
        Pageable pageable
    );
}
