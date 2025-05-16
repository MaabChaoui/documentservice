package com.example.document.service;

import com.example.document.dto.DocumentRequest;
import com.example.document.dto.DocumentMetadataRequest;
import com.example.document.model.*;
import com.example.document.repository.CategoryRepository;
import com.example.document.repository.DepartmentRepository;
import com.example.document.repository.DocumentRepository;
import com.example.document.security.UserContext;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class DocumentService {

    private final DocumentRepository documentRepository;
    private final CategoryRepository categoryRepository;
    private final DepartmentRepository departmentRepository;

    public DocumentService(DocumentRepository documentRepository,
                           CategoryRepository categoryRepository,
                           DepartmentRepository departmentRepository) {
        this.documentRepository = documentRepository;
        this.categoryRepository = categoryRepository;
        this.departmentRepository = departmentRepository;
    }

    public Optional<Document> getDocumentById(Long id) {
        return documentRepository.findById(id);  // Use repository's findById method
    }

    public Document createDocument(DocumentRequest request) {
        List<Integer> userDepartments = UserContext.get().getDepartments();

        if (!userDepartments.contains(request.getDepartmentId().intValue())) {
            throw new RuntimeException("Unauthorized department access");
        }

        Optional<Category> categoryOpt = categoryRepository.findById(request.getCategoryId());
        Optional<Department> departmentOpt = departmentRepository.findById(request.getDepartmentId());

        if (categoryOpt.isEmpty() || departmentOpt.isEmpty()) {
            throw new RuntimeException("Invalid category or department");
        }

        Document document = Document.builder()
                .title(request.getTitle())
                .category(categoryOpt.get())
                .department(departmentOpt.get())
                .createdBy(UserContext.get().getEmail())
                .status(DocumentStatus.PENDING)
                .creationDate(LocalDateTime.now())
                .fileUrl(request.getFileUrl())  // Set file URL from S3
                .build();

        return documentRepository.save(document);
    }

    public List<Document> getMyDocuments() {
        List<Integer> userDepartments = UserContext.get().getDepartments();
        List<Long> deptIds = userDepartments.stream()
                .map(Integer::longValue)
                .collect(Collectors.toList());

        List<Department> departments = departmentRepository.findAllById(deptIds);
        return documentRepository.findByDepartmentIn(departments);
    }

    public Document updateMetadata(Long documentId, DocumentMetadataRequest request) {
        Document document = documentRepository.findById(documentId)
                .orElseThrow(() -> new RuntimeException("Document not found"));

        if (!document.getCreatedBy().equals(UserContext.get().getEmail())) {
            throw new RuntimeException("You are not the creator of this document");
        }

        if (request.getStatus() != null) {
            document.setStatus(DocumentStatus.valueOf(request.getStatus().toUpperCase()));
        }

        if (request.getTitle() != null && !request.getTitle().isBlank()) {
            document.setTitle(request.getTitle());
        }
        return documentRepository.save(document);
    }

    public void deleteDocument(Long id) {
        Document document = documentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Document not found"));

        if (!document.getCreatedBy().equals(UserContext.get().getEmail())) {
            throw new RuntimeException("You are not allowed to delete this document");
        }

        documentRepository.delete(document);
    }
}
