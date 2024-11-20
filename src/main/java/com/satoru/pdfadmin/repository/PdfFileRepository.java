package com.satoru.pdfadmin.repository;

import com.satoru.pdfadmin.entity.PdfFile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.sql.Timestamp;
import java.util.List;

@Repository
public interface PdfFileRepository extends JpaRepository<PdfFile, Long> {
    List<PdfFile> findAllByCreatedAt(String createdAt);

}
