package com.example.document.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

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
}
