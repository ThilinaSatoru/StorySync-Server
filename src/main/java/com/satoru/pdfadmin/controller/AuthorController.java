package com.satoru.pdfadmin.controller;

import com.satoru.pdfadmin.entity.Author;
import com.satoru.pdfadmin.repository.AuthorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/author")
@CrossOrigin(origins = "http://localhost:3000")
public class AuthorController {

    private final AuthorRepository authorRepository;

    @Autowired
    public AuthorController(AuthorRepository authorRepository) {
        this.authorRepository = authorRepository;
    }

    // http://localhost:8080/author
    @GetMapping
    public List<Author> getAllFiles() {
        return authorRepository.findAll();
    }

    // 4. POST - Create a new
    // Endpoint: http://localhost:8080/author
    @PostMapping
    public ResponseEntity<Author> createFile(@RequestBody Author author) {
        Author savedAuthor = authorRepository.save(author);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedAuthor);
    }


    // 5. PUT - Update an existing file by ID
    // Endpoint: http://localhost:8080/file/{id}
    @PutMapping(value = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Author> updateFile(@PathVariable Long id, @RequestBody Author updatedFile) {
        Author savedFile = authorRepository.save(updatedFile);
        return ResponseEntity.status(HttpStatus.ACCEPTED).body(savedFile);
    }

    // 6. DELETE - Delete a file by ID
    // Endpoint: http://localhost:8080/file/{id}
    @DeleteMapping("/{id}")
    public ResponseEntity<Object> deleteFile(@PathVariable Long id) {
        return authorRepository.findById(id)
                .map(file -> {
                    authorRepository.delete(file);
                    return ResponseEntity.noContent().build();
                })
                .orElse(ResponseEntity.notFound().build());
    }
}
