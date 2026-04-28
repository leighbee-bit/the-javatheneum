package org.lmdlspfinal.bookstore_backend.services;

import org.lmdlspfinal.bookstore_backend.entities.WishlistItem;
import org.lmdlspfinal.bookstore_backend.repositories.WishlistItemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class WishlistItemService {

    @Autowired
    private WishlistItemRepository wishlistItemRepository;

    public WishlistItem addWishlistItem(WishlistItem wishlistItem) {
        return wishlistItemRepository.save(wishlistItem);
    }

    public List<WishlistItem> getItemsbyWishlist(Long wishlistid) {
        return wishlistItemRepository.findByWishlistWishlistid(wishlistid);
    }

    public void deleteWishlistItem(Long id) {
        wishlistItemRepository.deleteById(id);
    }
}
