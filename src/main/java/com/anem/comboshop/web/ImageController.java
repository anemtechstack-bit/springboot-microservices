package com.anem.comboshop.web;

import com.anem.comboshop.service.FileStorageService;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.nio.file.Files;
import java.nio.file.Path;

@Controller
public class ImageController {

    private final FileStorageService storage;
    public ImageController(FileStorageService storage){ this.storage = storage; }

    @GetMapping("/uploads")
    public ResponseEntity<Resource> view(@RequestParam String f) throws Exception{
        Path path = storage.resolve(f);
        if(!Files.exists(path)) return ResponseEntity.notFound().build();
        String ct = Files.probeContentType(path);
        if(ct == null) ct = MediaType.APPLICATION_OCTET_STREAM_VALUE;
        return ResponseEntity.ok().contentType(MediaType.parseMediaType(ct)).body(new FileSystemResource(path));
    }
}
