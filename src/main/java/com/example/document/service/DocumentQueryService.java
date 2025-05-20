package com.example.document.service;

import com.example.document.dto.DocumentResponse;
import com.example.document.model.Document;
import com.example.document.repository.DocumentRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import com.example.document.service.S3Service;

@Service
public class DocumentQueryService {

    private final DocumentRepository docRepo;
    private final S3Service          s3Service;

    @Autowired
    public DocumentQueryService(DocumentRepository docRepo,
                                S3Service s3Service) {
        this.docRepo    = docRepo;
        this.s3Service  = s3Service;
    }

    public Page<DocumentResponse> list(int page, int perPage, String search) {
        Pageable pageable = PageRequest.of(
            Math.max(page - 1, 0),
            perPage,
            Sort.by("createdAt").descending()
        );

        Page<Document> slice;
        if (search != null && !search.isBlank()) {
            slice = docRepo
                .findByTitleContainingIgnoreCaseOrDepartment_NameContainingIgnoreCaseOrCategory_NameContainingIgnoreCaseOrCreatedByContainingIgnoreCase(
                    search, search, search, search, pageable
                );
        } else {
            slice = docRepo.findAll(pageable);
        }

        // map each Document → DocumentResponse(doc, presignedUrl)
        return slice.map(doc -> {
            String url = s3Service.generatePresignedUrl(doc.getFileUrl());
            return new DocumentResponse(doc, url);
        });
    }
}
