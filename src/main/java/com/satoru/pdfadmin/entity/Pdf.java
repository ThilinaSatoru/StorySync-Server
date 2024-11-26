package com.satoru.pdfadmin.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import lombok.*;

import java.util.Objects;
import java.util.Set;

@Getter
@Setter
@RequiredArgsConstructor
@AllArgsConstructor
@Entity
@Builder
@Table(name = "pdf")
public class Pdf {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "filepath")
    private String filePath;

    @Column(name = "filename")
    private String fileName;

    @Column(name = "file_size")
    private Integer fileSize;

    @Column(name = "language")
    @Enumerated(EnumType.STRING)
    private Language language;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Column(name = "created_time")
    private String createdTime;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Column(name = "modified_time")
    private String modifiedTime;

    @Column(name = "page_count")
    private Integer pageCount;

    @Column(name = "title")
    private String title;

    @Column(name = "author")
    private String author;

    @ManyToMany(cascade = { CascadeType.PERSIST, CascadeType.MERGE })
    @JoinTable(name = "pdf_tags", joinColumns = @JoinColumn(name = "pdf_id"), inverseJoinColumns = @JoinColumn(name = "tag_id"))
    private Set<Tag> tags;

    @ManyToMany(cascade = { CascadeType.PERSIST, CascadeType.MERGE })
    @JoinTable(name = "pdf_authors", joinColumns = @JoinColumn(name = "pdf_id"), inverseJoinColumns = @JoinColumn(name = "author_id"))
    private Set<Author> authors;

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        Pdf pdf = (Pdf) o;
        return Objects.equals(id, pdf.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}