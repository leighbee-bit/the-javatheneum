package org.lmdlspfinal.bookstore_backend.entities;

import jakarta.persistence.*;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Entity
@Table(name = "books")
public class Book {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long bookid;

    @Column(nullable = false)
    private String title;

    @Column (nullable = false)
    private String author;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal price;

    @Column(name = "image_url")
    private String imageUrl;

    @Column(name = "page_count")
    private Integer pageCount;

    @Column(name = "preview_link", length = 500)
    private String previewLink;

    @Column
    private String genre;

    @Column(columnDefinition = "integer default 0")
    private Integer wishlistCount = 0;
}
