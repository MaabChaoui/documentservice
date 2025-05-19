package com.example.document.controller;

import java.util.List;
import java.util.Optional;  // Import this class
import java.util.stream.Collectors;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.example.document.dto.DocumentMetadataRequest;
import com.example.document.dto.DocumentRequest;
import com.example.document.dto.DocumentWithDownloadUrl;
import com.example.document.model.Document;
import com.example.document.service.DocumentService;
import com.example.document.service.S3Service;

@RestController
@RequestMapping("/api/documents")
public class DocumentController {

    private final DocumentService documentService;
    private final S3Service s3Service;

    public DocumentController(DocumentService documentService, S3Service s3Service) {
        this.documentService = documentService;
        this.s3Service = s3Service;
    }

    // Create a document with a file upload
    @PostMapping
    public Document create(@RequestParam("file") MultipartFile file,
            @RequestParam("title") String title,
            @RequestParam("categoryId") Long categoryId,
            @RequestParam("departmentId") Long departmentId) {
        try {
            // Upload file to S3 and get the file name
            String fileName = s3Service.uploadFile(file);

            // Create DocumentRequest with the data
            DocumentRequest request = new DocumentRequest();
            request.setTitle(title);
            request.setCategoryId(categoryId);
            request.setDepartmentId(departmentId);
            request.setFileUrl(fileName);  // Set the file URL after uploading to S3

            // Create and return the document
            return documentService.createDocument(request);
        } catch (Exception e) {
            throw new RuntimeException("Error uploading file: " + e.getMessage(), e);
        }
    }

    // Get a document by ID and generate a pre-signed URL for downloading
    @GetMapping("/{id}")
    public DocumentWithDownloadUrl getDocumentDownloadUrl(@PathVariable Long id) {
        Optional<Document> documentOpt = documentService.getDocumentById(id);

        if (documentOpt.isEmpty()) {
            throw new RuntimeException("Document not found");
        }

        Document document = documentOpt.get();
        String fileName = document.getFileUrl();  // Assuming file URL is stored in the 'fileUrl' field

        // Generate a pre-signed URL for the document
        try {
            String presignedUrl = s3Service.generatePresignedUrl(fileName);

            // Return metadata along with pre-signed URL
            return new DocumentWithDownloadUrl(
                    document.getTitle(),
                    document.getCategory().getName(), // Assuming category has a name
                    document.getDepartment().getName(), // Assuming department has a name
                    presignedUrl
            );
        } catch (Exception e) {
            throw new RuntimeException("Error generating pre-signed URL: " + e.getMessage(), e);
        }
    }

    // Update metadata (title, status)
    @PutMapping("/{id}/metadata")
    public Document updateMetadata(@PathVariable Long id, @RequestBody DocumentMetadataRequest request) {
        return documentService.updateMetadata(id, request);  // Ensure you're passing DocdumentMetadataRequest
    }

    // Get all documents related to the user's departments and include pre-signed download URLs
    // @GetMapping
    // public List<DocumentWithDownloadUrl> getMyDocuments() {
    //     List<Document> documents = documentService.getMyDocuments();
    //     // Map documents to include download URLs
    //     return documents.stream().map(document -> {
    //         String fileName = document.getFileUrl();  // Assuming file URL is stored in 'fileUrl'
    //         // Generate a pre-signed URL for the document
    //         try {
    //             String presignedUrl = s3Service.generatePresignedUrl(fileName);
    //             // Return metadata along with pre-signed URL
    //             return new DocumentWithDownloadUrl(
    //                 // TODO
    //                 document.getId(),
    //                 document.getTitle(),
    //                 document.getCategory().getName(),  // Assuming category has a name
    //                 document.getDepartment().getName(),  // Assuming department has a name
    //                 presignedUrl
    //             );
    //         } catch (Exception e) {
    //             throw new RuntimeException("Error generating pre-signed URL: " + e.getMessage(), e);
    //         }
    //     }).collect(Collectors.toList());
    // }
    @GetMapping
    public List<DocumentWithDownloadUrl> getMyDocuments() {
        List<Document> documents = documentService.getMyDocuments();

        // Map documents to include download URLs
        return documents.stream().map(document -> {
            String fileName = document.getFileUrl();  // Assuming file URL is stored in 'fileUrl'

            try {
                String presignedUrl = s3Service.generatePresignedUrl(fileName);

                return new DocumentWithDownloadUrl(
                        document.getId(),
                        document.getTitle(),
                        document.getCategory().getName(),
                        document.getDepartment().getName(),
                        document.getCreatedAt(), // assuming field is called createdAt (LocalDateTime)
                        document.getCreatedBy(), // assuming createdBy is a User entity
                        document.getStatus(),
                        presignedUrl
                );
            } catch (Exception e) {
                throw new RuntimeException("Error generating pre-signed URL: " + e.getMessage(), e);
            }
        }).collect(Collectors.toList());
    }

    // Delete a document (only creator allowed) and remove the file from S3
    @DeleteMapping("/{id}")
    public void deleteDocument(@PathVariable Long id) {
        Optional<Document> documentOpt = documentService.getDocumentById(id);
        if (documentOpt.isEmpty()) {
            throw new RuntimeException("Document not found");
        }

        Document document = documentOpt.get();

        // Fetch the file URL (which is used as the object name in S3)
        String fileName = document.getFileUrl();

        // Delete file from S3
        try {
            s3Service.deleteFile(fileName); // Ensure this method exists in S3Service
        } catch (Exception e) {
            throw new RuntimeException("Error deleting file from S3: " + e.getMessage(), e);
        }

        // Delete document from database
        documentService.deleteDocument(id);
    }

}
