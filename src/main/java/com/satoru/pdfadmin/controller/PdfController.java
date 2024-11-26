package com.satoru.pdfadmin.controller;

import com.satoru.pdfadmin.entity.Pdf;
import com.satoru.pdfadmin.repository.PdfFileRepository;
import com.satoru.pdfadmin.service.PdfFileService;
import com.satoru.pdfadmin.service.PdfNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController()
@RequestMapping("/file")
@CrossOrigin(origins = "http://localhost:3000")
public class PdfFileController {
    private final PdfFileService fileService;
    private final PdfFileRepository pdfFileRepository;

    @Autowired
    public PdfFileController(PdfFileService fileService, PdfFileRepository pdfFileRepository) {
        this.fileService = fileService;
        this.pdfFileRepository = pdfFileRepository;
    }

    // http://localhost:8080/file
    @GetMapping
    public List<Pdf> getAllFiles() {
        return fileService.getAllFiles();
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
        return pdfFileRepository.findAllByCreatedTime(date);
    }

    // 4. POST - Create a new file
    // Endpoint: http://localhost:8080/file
    @PostMapping
    public ResponseEntity<Pdf> createFile(@RequestBody Pdf pdf) {
        Pdf savedFile = pdfFileRepository.save(pdf);
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
        return pdfFileRepository.findById(id)
                .map(file -> {
                    pdfFileRepository.delete(file);
                    return ResponseEntity.noContent().build();
                })
                .orElse(ResponseEntity.notFound().build());
    }


}