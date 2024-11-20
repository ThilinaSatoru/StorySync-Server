package com.satoru.pdfadmin.repository;

import com.satoru.pdfadmin.entity.Author;
import com.satoru.pdfadmin.entity.Tag;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AuthorRepository extends JpaRepository<Author, Long> {
    Optional<Author> findByName(String name);
}
