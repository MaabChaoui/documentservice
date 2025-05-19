package com.example.document.repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.document.dto.DepartmentSummary;
import com.example.document.model.Department;

public interface DepartmentRepository extends JpaRepository<Department, Long> {

    /**
     * Returns every department with how many users are assigned to it.
     */
    @Query("""
           SELECT d.id   AS id,
                  d.name AS name,
                  d.createdAt AS createdAt,
                  COUNT(u.id) AS usersCount
           FROM Department d
           LEFT JOIN d.users u
           GROUP BY d.id, d.name, d.createdAt
           ORDER BY COUNT(u.id) DESC
           """)
    List<DepartmentSummary> findAllWithUserCount();

    @Query("""
        SELECT d.id        AS id,
               d.name      AS name,
               d.createdAt AS createdAt,
               COUNT(u.id) AS usersCount
        FROM Department d
        LEFT JOIN d.users u
        WHERE d.id IN :ids
        GROUP BY d.id, d.name, d.createdAt
        ORDER BY COUNT(u.id) DESC
        """)
    List<DepartmentSummary> findByIdsWithUserCount(@Param("ids") List<Long> ids);

    Optional<Department> findByName(String name);

    @Query("""
   SELECT dept.name AS name,
          COUNT(d.id) AS cnt
   FROM   Department dept
   LEFT   JOIN Document d ON d.department = dept
   GROUP  BY dept.name
""")
    List<Map<String, Object>> findDocumentCountsPerDepartment();

    long count();

    long countByCreatedAtAfter(LocalDateTime cutoff);
}
