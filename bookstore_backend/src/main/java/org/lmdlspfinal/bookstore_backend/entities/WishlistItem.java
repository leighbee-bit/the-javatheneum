package org.lmdlspfinal.bookstore_backend.entities;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "wishlist_items")
public class WishlistItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long wishlistitemid;

    @ManyToOne
    @JoinColumn(name = "wishlistid", nullable = false)
    @JsonBackReference
    private Wishlist wishlist;

    @ManyToOne
    @JoinColumn(name = "bookid", nullable = false)
    private Book book;

    @Column(name = "added_at", updatable = false)
    private LocalDateTime addedAt;

    @PrePersist
    protected void onAdd() {
        addedAt = LocalDateTime.now();
    }
}