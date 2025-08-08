package com.lms.repository.impl;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URI;
import java.util.Objects;
import java.util.UUID;


import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Repository;
import org.springframework.core.io.FileSystemResource;

// import org.springframework.util.FileCopyUtils;
import org.springframework.web.multipart.MultipartFile;

import com.lms.repository.StorageRepository;

import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.core.ResponseInputStream;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.S3Configuration;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.model.GetObjectResponse;

@Repository
public class SupabaseStorageRepository implements StorageRepository {

    private final S3Client s3Client;

    @Value("${supabase.s3.bucket}")
    private String bucket;

    public SupabaseStorageRepository(
            @Value("${supabase.s3.access-key}") String accessKey,
            @Value("${supabase.s3.secret-key}") String secretKey,
            @Value("${supabase.s3.region}") String region,
            @Value("${supabase.s3.endpoint}") String endpoint
    ) {
        AwsBasicCredentials credentials = AwsBasicCredentials.create(accessKey, secretKey);
        this.s3Client = S3Client.builder()
                .credentialsProvider(StaticCredentialsProvider.create(credentials))
                .endpointOverride(URI.create(endpoint))
                .region(Region.of(region))
                .serviceConfiguration(S3Configuration.builder()
                        .pathStyleAccessEnabled(true)
                        .build())
                .build();
    }

    @Override
    public String store(MultipartFile multipartFile) {
        String extension = Objects.requireNonNull(multipartFile.getOriginalFilename())
                                  .substring(multipartFile.getOriginalFilename().lastIndexOf("."));
        String fileName = UUID.randomUUID().toString().replaceAll("-", "") + extension;

        try {
            PutObjectRequest putRequest = PutObjectRequest.builder()
                    .bucket(bucket)
                    .key(fileName)
                    .contentType(multipartFile.getContentType())
                    .build();

            s3Client.putObject(putRequest, RequestBody.fromInputStream(multipartFile.getInputStream(), multipartFile.getSize()));
            return fileName;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public Resource load(String fileName) {
        try {
            GetObjectRequest getRequest = GetObjectRequest.builder()
                    .bucket(bucket)
                    .key(fileName)
                    .build();

            ResponseInputStream<GetObjectResponse> inputStream = s3Client.getObject(getRequest);

            // Write to temp file (Spring needs a Resource)
            File tempFile = File.createTempFile("download_", "_" + fileName);
            try (FileOutputStream out = new FileOutputStream(tempFile)) {
                inputStream.transferTo(out);
            }

            return new FileSystemResource(tempFile);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
