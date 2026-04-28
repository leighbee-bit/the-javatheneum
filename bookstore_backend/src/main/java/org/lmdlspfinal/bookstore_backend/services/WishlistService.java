package org.lmdlspfinal.bookstore_backend.services;


import org.lmdlspfinal.bookstore_backend.entities.Wishlist;
import org.lmdlspfinal.bookstore_backend.repositories.WishlistRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class WishlistService {

    @Autowired
    public WishlistRepository wishlistRepository;

    public Wishlist createWishlist(Wishlist wishlist) {
        return wishlistRepository.save(wishlist);
    }

    public List<Wishlist> getWishlistsByUser(Long userId) {
        return wishlistRepository.findByUserUserid(userId);
    }

    public Optional<Wishlist> getWishlistById(Long id) {
        return wishlistRepository.findById(id);
    }

    public void deleteWishlist(Long id) {
        wishlistRepository.deleteById(id);
    }
}
