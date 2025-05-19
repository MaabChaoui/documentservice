package com.example.document.repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.example.document.dto.CategorySummary;
import com.example.document.model.Category;

public interface CategoryRepository extends JpaRepository<Category, Long> {

    Optional<Category> findByName(String name);

    /**
     * Returns every category and the number of documents assigned to it
     */
    @Query("""
       SELECT c.id          AS id,
              c.name        AS name,
              c.createdAt   AS createdAt,
              COUNT(d.id)   AS docsCount
       FROM   Category c
       LEFT   JOIN Document d ON d.category = c
       GROUP  BY c.id, c.name, c.createdAt
       ORDER  BY c.name
       """)
    List<CategorySummary> findAllWithDocumentCount();

    @Query("""
   SELECT c.name      AS name,
          COUNT(d.id) AS cnt
   FROM   Category c
   LEFT   JOIN Document d ON d.category = c
   GROUP  BY c.name
""")
    List<Map<String, Object>> findDocumentCountsPerCategory();

    long count();

    long countByCreatedAtAfter(LocalDateTime cutoff);
}
