package com.lms.repository;

import org.springframework.core.io.Resource;
import org.springframework.stereotype.Repository;
import org.springframework.web.multipart.MultipartFile;

@Repository
public interface StorageRepository {

    String store(MultipartFile multipartFile);
    Resource load(String fileName);

}
