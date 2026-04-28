package org.lmdlspfinal.bookstore_frontend.models;

import java.math.BigDecimal;

public class Book {
    private Long bookid;
    private String title;
    private String author;
    private String description;
    private BigDecimal price;
    private String imageUrl;

    private Integer pageCount;
    private String previewLink;

    private String genre;
    private Integer wishlistCount;

    public String getGenre() { return genre; }
    public void setGenre(String genre) { this.genre = genre; }
    public Integer getWishlistCount() { return wishlistCount; }
    public void setWishlistCount(Integer wishlistCount) { this.wishlistCount = wishlistCount; }

    public Integer getPageCount() { return pageCount; }
    public void setPageCount(Integer pageCount) { this.pageCount = pageCount; }
    public String getPreviewLink() { return previewLink; }
    public void setPreviewLink(String previewLink) { this.previewLink = previewLink; }

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