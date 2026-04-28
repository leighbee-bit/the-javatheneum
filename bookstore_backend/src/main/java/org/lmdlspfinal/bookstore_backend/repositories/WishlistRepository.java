package org.lmdlspfinal.bookstore_backend.repositories;

import org.lmdlspfinal.bookstore_backend.entities.Wishlist;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface WishlistRepository extends JpaRepository<Wishlist, Long> {
    List<Wishlist> findByUserUserid(Long userid);
}