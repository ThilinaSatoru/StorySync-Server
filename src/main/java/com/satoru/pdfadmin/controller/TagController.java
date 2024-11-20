package com.satoru.pdfadmin.controller;

import com.satoru.pdfadmin.entity.Author;
import com.satoru.pdfadmin.entity.PdfFile;
import com.satoru.pdfadmin.entity.Tag;
import com.satoru.pdfadmin.repository.PdfFileRepository;
import com.satoru.pdfadmin.repository.TagRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/tag")
@CrossOrigin(origins = "http://localhost:3000")
public class TagController {

    private final TagRepository tagRepository;

    @Autowired
    public TagController(TagRepository tagRepository) {
        this.tagRepository = tagRepository;
    }

    // http://localhost:8080/tag
    @GetMapping
    public List<Tag> getAllFiles() {
        return tagRepository.findAll();
    }

    // 4. POST - Create a new
    // Endpoint: http://localhost:8080/tag
    @PostMapping
    public ResponseEntity<Tag> createFile(@RequestBody Tag tag) {
        Tag savedTag = tagRepository.save(tag);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedTag);
    }

    // 5. PUT - Update an existing file by ID
    // Endpoint: http://localhost:8080/file/{id}
    @PutMapping(value = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Tag> updateFile(@PathVariable Long id, @RequestBody Tag updatedFile) {
        Tag savedFile = tagRepository.save(updatedFile);
        return ResponseEntity.status(HttpStatus.ACCEPTED).body(savedFile);
    }

    // 6. DELETE - Delete a file by ID
    // Endpoint: http://localhost:8080/file/{id}
    @DeleteMapping("/{id}")
    public ResponseEntity<Object> deleteFile(@PathVariable Long id) {
        return tagRepository.findById(id)
                .map(file -> {
                    tagRepository.delete(file);
                    return ResponseEntity.noContent().build();
                })
                .orElse(ResponseEntity.notFound().build());
    }
}
