package com.anem.comboshop.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.*;
import java.time.LocalDateTime;

@Service
public class FileStorageService {
    private final Path rootDir;

    public FileStorageService(@Value("${app.upload-dir:uploads}") String uploadDir) throws IOException {
        this.rootDir = Paths.get(uploadDir).toAbsolutePath().normalize();
        Files.createDirectories(this.rootDir);
    }

    public String saveOptional(MultipartFile file) throws IOException {
        if(file == null || file.isEmpty()) return null;
        String original = StringUtils.cleanPath(file.getOriginalFilename()==null ? "image" : file.getOriginalFilename());
        String safe = original.replaceAll("[^a-zA-Z0-9._-]", "_");
        String filename = LocalDateTime.now().toString().replace(":", "-") + "_" + safe;
        Path target = rootDir.resolve(filename).normalize();
        Files.copy(file.getInputStream(), target, StandardCopyOption.REPLACE_EXISTING);
        return filename;
    }

    public Path resolve(String filename){
        return rootDir.resolve(filename).normalize();
    }
}
