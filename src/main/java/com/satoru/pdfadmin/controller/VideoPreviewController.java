package com.satoru.pdfadmin.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.nio.file.Paths;

import static com.satoru.pdfadmin.config.WebConfig.getResourceResponseVideoEntity;

@Slf4j
@RestController
@RequestMapping("/api/video")
@RequiredArgsConstructor
public class VideoPreviewController {

//    http://localhost:8080/api/video/preview?fileName=E%3A%5C%5CDowns%5C%5Cbase%5C%5Cvideos%5C%5Cpreviews%5C%5Ccuck%5C%5Cvideo_2024-10-26_09-13-23_preview.mp4
    @GetMapping("/preview")
    public ResponseEntity<?> getVideoPreview(@RequestParam String fileName) {
        try {
            // Decode the file path
            String decodedPath = URLDecoder.decode(fileName, StandardCharsets.UTF_8);
            Path filePath = Paths.get(decodedPath);
            Resource resource = new UrlResource(filePath.toUri());

            System.out.println("Decoded File Path: " + decodedPath);
            System.out.println("Full Path Resolved: " + filePath);

            // Check if preview exists
            if (resource.exists() && resource.isReadable()) {
                return getResourceResponseVideoEntity(resource);
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
            }

        } catch (Exception e) {
            log.error("Error serving video preview for filename: " + fileName, e);
            return ResponseEntity.internalServerError().build();
        }
    }
}