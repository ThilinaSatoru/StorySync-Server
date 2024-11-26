package com.satoru.pdfadmin.repository;

import com.satoru.pdfadmin.entity.Pdf;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PdfRepository extends JpaRepository<Pdf, Long> {
    List<Pdf> findAllByCreatedTime(String createdAt);

}
