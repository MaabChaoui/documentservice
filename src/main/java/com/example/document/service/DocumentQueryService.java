package com.example.document.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.example.document.dto.DocumentResponse;
import com.example.document.model.Document;
import com.example.document.repository.DocumentRepository;

@Service
public class DocumentQueryService {

    private final DocumentRepository docRepo;

    @Autowired
    public DocumentQueryService(DocumentRepository docRepo) {
        this.docRepo = docRepo;
    }

    public Page<DocumentResponse> list(int page, int perPage, String search) {
        Pageable pageable = PageRequest.of(
            Math.max(page - 1, 0),
            perPage,
            Sort.by("createdAt").descending()
        );

        Page<Document> slice;
        if (search != null && !search.isBlank()) {
            slice = docRepo.findByTitleContainingIgnoreCaseOrDepartment_NameContainingIgnoreCaseOrCategory_NameContainingIgnoreCase(
                search, search, search, pageable
            );
        } else {
            slice = docRepo.findAll(pageable);
        }

        return slice.map(DocumentResponse::new);
    }
}
