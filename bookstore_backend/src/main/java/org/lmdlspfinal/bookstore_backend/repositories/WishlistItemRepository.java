package org.lmdlspfinal.bookstore_backend.repositories;

import org.lmdlspfinal.bookstore_backend.entities.WishlistItem;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface WishlistItemRepository extends JpaRepository<WishlistItem, Long> {
    List<WishlistItem> findByWishlistWishlistid(Long wishlistid);
}