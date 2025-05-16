package com.example.document.service;

import io.minio.*;
import io.minio.errors.MinioException;
import io.minio.messages.Bucket;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import io.minio.http.Method;

import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.List;

@Service
public class S3Service {

    private final MinioClient minioClient;
    private final String bucketName;

    public S3Service(@Value("${s3.endpoint}") String endpoint,
                     @Value("${s3.access-key}") String accessKey,
                     @Value("${s3.secret-key}") String secretKey,
                     @Value("${s3.bucket-name}") String bucketName) throws Exception {
        // Using MinioClient.builder() for the new SDK
        this.minioClient = MinioClient.builder()
                                      .endpoint(endpoint)
                                      .credentials(accessKey, secretKey)
                                      .build();
        this.bucketName = bucketName;

        // Ensure the bucket exists or create it
        if (!minioClient.bucketExists(BucketExistsArgs.builder().bucket(bucketName).build())) {
            minioClient.makeBucket(MakeBucketArgs.builder().bucket(bucketName).build());
        }
    }

    // Method to upload a file
    public String uploadFile(MultipartFile file) throws IOException, MinioException {
        String fileName = System.currentTimeMillis() + "_" + file.getOriginalFilename();
        try {
            minioClient.putObject(
                PutObjectArgs.builder()
                    .bucket(bucketName)
                    .object(fileName)
                    .stream(file.getInputStream(), file.getSize(), -1)
                    .contentType(file.getContentType())
                    .build());
        } catch (MinioException | IOException | NoSuchAlgorithmException | InvalidKeyException e) {
            throw new IOException("Error uploading file to S3", e);
        }
        return fileName;
    }

    // Method to generate a pre-signed URL for downloading a file
    public String generatePresignedUrl(String fileName) {
        try {
            System.out.println("File " + fileName + " has been successfully deleted from S3.");
            // Ensure method is set to GET for download
            return minioClient.getPresignedObjectUrl(
                GetPresignedObjectUrlArgs.builder()
                    .method(Method.GET)  // Make sure the method is GET for download
                    .bucket(bucketName)
                    .object(fileName)
                    .build());
        } catch (MinioException | IOException e) {
            throw new RuntimeException("Error generating pre-signed URL", e);
        } catch (InvalidKeyException | NoSuchAlgorithmException e) {
            throw new RuntimeException("Error with the key or algorithm during pre-signed URL generation", e);
        }
    }

    // Method to delete a file from S3
    public void deleteFile(String fileName) {
        try {
            minioClient.removeObject(
                RemoveObjectArgs.builder()
                    .bucket(bucketName)
                    .object(fileName)
                    .build());
        } catch (MinioException | IOException | InvalidKeyException | NoSuchAlgorithmException e) {
            throw new RuntimeException("Error deleting file from S3", e);
        }
    }


    // List buckets (for debugging or checking)
    public List<Bucket> listBuckets() {
        try {
            return minioClient.listBuckets();
        } catch (MinioException | IOException | InvalidKeyException | NoSuchAlgorithmException e) {
            throw new RuntimeException("Error listing buckets", e);  // Handle IOException and InvalidKeyException
        }
    }
}
