package com.satoru.pdfadmin.service;

import com.satoru.pdfadmin.entity.Author;
import com.satoru.pdfadmin.entity.Pdf;
import com.satoru.pdfadmin.entity.Tag;
import com.satoru.pdfadmin.repository.AuthorRepository;
import com.satoru.pdfadmin.repository.PdfRepository;
import com.satoru.pdfadmin.repository.TagRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class PdfService {
    private final PdfRepository fileRepository;
    private final TagRepository tagRepository;

    private final AuthorRepository authorRepository;

    @Autowired
    public PdfService(PdfRepository fileRepository, TagRepository tagRepository, AuthorRepository authorRepository) {
        this.fileRepository = fileRepository;
        this.tagRepository = tagRepository;
        this.authorRepository = authorRepository;
    }

    public List<Pdf> getAllFiles() {
        System.out.println("Fetching Files....");
        return fileRepository.findAll();
    }

    public Pdf getFileById(Long id) throws PdfNotFoundException {
        System.out.printf("Fetching Files by Id :: [%s]", id);
        return fileRepository.findById(id).orElseThrow(() -> new PdfNotFoundException("File Not Found."));
    }

    @Transactional
    public Pdf updateFile(Pdf updatedFile) throws PdfNotFoundException {
        System.out.printf("PdfFile [%s] is updating... :: %s ", updatedFile.getId(), updatedFile.getFileName());
        // Fetch existing PdfFile
        Pdf existingFile = fileRepository.findById(updatedFile.getId())
                .orElseThrow(() -> new PdfNotFoundException("PdfFile with ID " + updatedFile.getId() + " not found"));

        // Update basic fields
        existingFile.setFilePath(updatedFile.getFilePath());
        existingFile.setFileName(updatedFile.getFileName());
//        existingFile.setFolderName(updatedFile.getFolderName());
//        existingFile.setCover(updatedFile.getCover());
//        existingFile.setFileType(updatedFile.getFileType());
//        existingFile.setLanguage(updatedFile.getLanguage());
//        existingFile.setLastModified(updatedFile.getLastModified());

        // Handle Tags: Ensure existing tags are kept and new ones are added
        Set<Tag> updatedTags = updatedFile.getTags().stream()
                .map(tag -> tagRepository.findByName(tag.getName())
                        .orElseGet(() -> tagRepository.save(new Tag(null, tag.getName(), null, null, null))))
                .collect(Collectors.toSet());
        existingFile.setTags(updatedTags);

        Set<Author> updatedAuthors = updatedFile.getAuthors().stream()
                .map(tag -> authorRepository.findByName(tag.getName())
                        .orElseGet(() -> authorRepository.save(new Author(null, tag.getName(), null, null, null))))
                .collect(Collectors.toSet());
        existingFile.setAuthors(updatedAuthors);

        // Save updated PdfFile
        return fileRepository.save(existingFile);
    }


}
