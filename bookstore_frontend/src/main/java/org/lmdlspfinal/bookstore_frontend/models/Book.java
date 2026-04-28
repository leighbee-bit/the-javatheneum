package org.lmdlspfinal.bookstore_frontend.models;

import java.math.BigDecimal;

public class Book {
    private Long bookid;
    private String title;
    private String author;
    private String description;
    private BigDecimal price;
    private String imageUrl;

    public Long getBookid() { return bookid; }
    public void setBookid(Long bookid) { this.bookid = bookid; }
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
    public String getAuthor() { return author; }
    public void setAuthor(String author) { this.author = author; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public BigDecimal getPrice() { return price; }
    public void setPrice(BigDecimal price) { this.price = price; }
    public String getImageUrl() { return imageUrl; }
    public void setImageUrl(String imageUrl) { this.imageUrl = imageUrl; }

    @Override
    public String toString() { return title + " by " + author; }
}