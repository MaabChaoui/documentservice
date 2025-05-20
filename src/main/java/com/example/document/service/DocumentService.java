package com.example.document.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.example.document.dto.DocumentMetadataRequest;
import com.example.document.dto.DocumentRequest;
import com.example.document.model.Category;
import com.example.document.model.Department;
import com.example.document.model.Document;
import com.example.document.repository.CategoryRepository;
import com.example.document.repository.DepartmentRepository;
import com.example.document.repository.DocumentRepository;
import com.example.document.security.UserContext;

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
                .status("PENDING")
                .createdAt(LocalDateTime.now())
                .fileUrl(request.getFileUrl()) // Set file URL from S3
                .type(request.getType())
                .size(request.getSize())
                .build();

        return documentRepository.save(document);
    }

    public List<Document> getMyDocuments() {
        String email = UserContext.get().getEmail();
        return documentRepository.findAllVisibleToUserByEmail(email);
    }

    // public Document updateMetadata(Long documentId, DocumentMetadataRequest request) {
    //     Document document = documentRepository.findById(documentId)
    //             .orElseThrow(() -> new RuntimeException("Document not found"));
    //     if (!document.getCreatedBy().equals(UserContext.get().getEmail())) {
    //         throw new RuntimeException("You are not the creator of this document");
    //     }
    //     if (request.getStatus() != null) {
    //         document.setStatus(DocumentStatus.valueOf(request.getStatus().toUpperCase()));
    //     }
    //     if (request.getTitle() != null && !request.getTitle().isBlank()) {
    //         document.setTitle(request.getTitle());
    //     }
    //     Optional<Category> categoryOpt = categoryRepository.findByName(request.getCategory());
    //     Optional<Department> departmentOpt = departmentRepository.findByName(request.getDepartment());
    //     // update category and department if not null here
    //     return documentRepository.save(document);
    // }
    public Document updateMetadata(Long documentId, DocumentMetadataRequest request) {
        Document document = documentRepository.findById(documentId)
                .orElseThrow(() -> new RuntimeException("Document not found"));

        if (!document.getCreatedBy().equals(UserContext.get().getEmail())) {
            throw new RuntimeException("You are not the creator of this document");
        }

        // Update status if provided
        if (request.getStatus() != null) {
            document.setStatus(request.getStatus().toUpperCase());
        }

        // Update title if provided and not blank
        if (request.getTitle() != null && !request.getTitle().isBlank()) {
            document.setTitle(request.getTitle());
        }

        // Update category if provided and found
        if (request.getCategory() != null && !request.getCategory().isBlank()) {
            Optional<Category> categoryOpt = categoryRepository.findByName(request.getCategory());
            categoryOpt.ifPresent(document::setCategory);
        }

        // Update department if provided and found
        if (request.getDepartment() != null && !request.getDepartment().isBlank()) {
            Optional<Department> departmentOpt = departmentRepository.findByName(request.getDepartment());
            departmentOpt.ifPresent(document::setDepartment);
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
