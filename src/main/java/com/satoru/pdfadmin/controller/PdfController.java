package com.satoru.pdfadmin.controller;

import com.satoru.pdfadmin.entity.Pdf;
import com.satoru.pdfadmin.repository.PdfRepository;
import com.satoru.pdfadmin.service.PdfNotFoundException;
import com.satoru.pdfadmin.service.PdfService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController()
@RequestMapping("/api/pdf")
@CrossOrigin(origins = "http://localhost:3000")
public class PdfController {
    private final PdfService fileService;
    private final PdfRepository pdfRepository;

    @Autowired
    public PdfController(PdfService fileService, PdfRepository pdfRepository) {
        this.fileService = fileService;
        this.pdfRepository = pdfRepository;
    }

    // http://localhost:8080/file
    @GetMapping
    public ResponseEntity<List<Pdf>> getAllFiles() {
        return ResponseEntity.ok(fileService.getAllFiles());
    }

    // 2. GET - Fetch file by ID
    // Endpoint: http://localhost:8080/file/{id}
    @GetMapping("/{id}")
    public Pdf getFileById(@PathVariable Long id) throws PdfNotFoundException {
        return fileService.getFileById(id);
    }

    // http://localhost:8080/file/after?date=2024-11-18%2015:32:27
    @GetMapping("/after")
    public List<Pdf> getFilesCreatedAfter(@RequestParam("date") String date) {
        return pdfRepository.findAllByCreatedTime(date);
    }

    // 4. POST - Create a new file
    // Endpoint: http://localhost:8080/file
    @PostMapping
    public ResponseEntity<Pdf> createFile(@RequestBody Pdf pdf) {
        Pdf savedFile = pdfRepository.save(pdf);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedFile);
    }

    // 5. PUT - Update an existing file by ID
    // Endpoint: http://localhost:8080/file/{id}
    @PutMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Pdf> updateFile(@RequestBody Pdf updatedFile) throws PdfNotFoundException {
        Pdf savedFile = fileService.updateFile(updatedFile);
        return ResponseEntity.status(HttpStatus.ACCEPTED).body(savedFile);
    }

    // 6. DELETE - Delete a file by ID
    // Endpoint: http://localhost:8080/file/{id}
    @DeleteMapping("/{id}")
    public ResponseEntity<Object> deleteFile(@PathVariable Long id) {
        System.out.println("Deleting.. : File " + id);
        return pdfRepository.findById(id)
                .map(file -> {
                    pdfRepository.delete(file);
                    return ResponseEntity.noContent().build();
                })
                .orElse(ResponseEntity.notFound().build());
    }


}
