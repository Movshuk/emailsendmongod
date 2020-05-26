package com.email.service.serviceImpl;

import com.email.config.FileStorageProperties;
import com.email.exception.FileStorageException;
import com.email.repository.MessageRepository;
import com.email.service.FileStorageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

@Service
public class FileStorageServiceImpl implements FileStorageService {

    private final Path fileStorageLocation;
    @Autowired
    MessageRepository messageRepository;
    @Autowired
    MessageServiceImpl messageServiceImpl;

    @Autowired
    public FileStorageServiceImpl(FileStorageProperties fileStorageProperties) {
        this.fileStorageLocation = Paths.get(fileStorageProperties.getUploadDir());
        //.toAbsolutePath().normalize(); // full path!!!
        try {
            Files.createDirectories(this.fileStorageLocation);
        } catch (Exception ex) {
            throw new FileStorageException("Could not create the directory where the uploaded files will be stored.", ex);
        }
    }

    public Path storeFile(MultipartFile file) {
        // get file name
        String fileName = StringUtils.cleanPath(file.getOriginalFilename());

        try {
            // Check if the file's name contains invalid characters
            if(fileName.contains("..")) {
                throw new FileStorageException("Sorry! Filename contains invalid path sequence " + fileName);
            }

            // Copy file to the target location (Replacing existing file with the same name)
            Path targetLocation = this.fileStorageLocation.resolve(fileName);
            Files.copy(file.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);
            //return fileName;
            return targetLocation;
        } catch (IOException ex) {
            throw new FileStorageException("Could not store file " + fileName + ". Please try again!", ex);
        }
    }

    public void delete(String attachment) {
        if (messageServiceImpl.existAttachment(attachment)) {
            return;
        }
        try {
            Path targetLocation = this.fileStorageLocation.resolve(attachment);
            Files.deleteIfExists(targetLocation);

        } catch (IOException e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }

    public String getPath(String attachment) {
        Path targetLocation = this.fileStorageLocation.resolve(attachment);
        return targetLocation.toString();
    }


}
