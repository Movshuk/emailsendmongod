package com.email.service;

import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Path;

public interface FileStorageService {

    Path storeFile(MultipartFile file);
    void delete(String attachment);
    String getPath(String attachment);

}
