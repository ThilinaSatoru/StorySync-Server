package com.satoru.pdfadmin.controller;

import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.nio.file.Paths;

@RestController
@RequestMapping("/api/pdf")
@CrossOrigin(origins = "http://localhost:3000")
public class PdfFileController {

//    {{base_url}}/api/pdf/file?fileName=E%3A%5C%5CDowns%5C%5Cbase%5C%5Ccontent%5C%5Cpdf%5C%5Cmilf%5C%5CAluth%20Teacher%20Aunty.pdf
    @GetMapping("/file")
    public ResponseEntity<Resource> getPdf(@RequestParam("fileName") String fileName) {
        try {
            // Decode the file path
            String decodedPath = URLDecoder.decode(fileName, StandardCharsets.UTF_8);
            Path filePath = Paths.get(decodedPath);
            Resource resource = new UrlResource(filePath.toUri());

            if (resource.exists() && resource.isReadable()) {
                return ResponseEntity.ok()
                        .contentType(MediaType.APPLICATION_PDF)
                        .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"" + filePath.getFileName() + "\"")
                        .body(resource);
            } else {
                return ResponseEntity.notFound().build();
            }

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}