package com.satoru.pdfadmin.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@RestController
@RequestMapping("/api/pdf")
@CrossOrigin(origins = "http://localhost:3000")
public class PdfController {

//    private final String baseDirectory = "E:\\Downs\\kat\\kath\\";
    @Value("${file.upload.root-dir}")
    private String baseDirectory;

    @GetMapping
    public ResponseEntity<Resource> getPdf(@RequestParam("file") String file) {
        try {
            // Decode the file path
            String decodedFilePath = URLDecoder.decode(file, StandardCharsets.UTF_8);

            // Ensure the file path is relative (remove leading '/')
            if (decodedFilePath.startsWith("/")) {
                decodedFilePath = decodedFilePath.substring(1);
            }

            // Construct the full path by resolving against the base directory
            Path resolvedPath = Paths.get(baseDirectory).resolve(decodedFilePath).normalize();

            // Ensure the resolved path is within the base directory for security
            Path basePath = Paths.get(baseDirectory).toAbsolutePath();
            Path fullPath = resolvedPath.toAbsolutePath();

            System.out.println("Decoded File Path: " + decodedFilePath);
            System.out.println("Full Path Resolved: " + fullPath);

            if (!fullPath.startsWith(basePath)) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
            }

            // Check if the file exists and is readable
            if (!Files.exists(resolvedPath) || !Files.isReadable(resolvedPath)) {
                return ResponseEntity.notFound().build();
            }

            // Load the file as a resource
            Resource resource = new UrlResource(resolvedPath.toUri());
            String contentType = Files.probeContentType(resolvedPath);

            if (contentType == null) {
                contentType = "application/octet-stream";
            }

            return ResponseEntity.ok()
                    .contentType(MediaType.parseMediaType(contentType))
                    .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"" + resolvedPath.getFileName() + "\"")
                    .body(resource);

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}