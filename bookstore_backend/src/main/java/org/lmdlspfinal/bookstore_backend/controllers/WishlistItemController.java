package org.lmdlspfinal.bookstore_backend.controllers;


import org.lmdlspfinal.bookstore_backend.entities.WishlistItem;
import org.lmdlspfinal.bookstore_backend.services.WishlistItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/wishlist-items")
public class WishlistItemController {

    @Autowired
    private WishlistItemService wishlistItemService;

    @PostMapping
    public ResponseEntity<WishlistItem> addWishlistItem(@RequestBody WishlistItem wishlistItem) {
        return ResponseEntity.ok(wishlistItemService.addWishlistItem(wishlistItem));
    }

    @GetMapping("/wishlist/{wishlistid}")
    public ResponseEntity<List<WishlistItem>> getItemsByWishlist(@PathVariable Long wishlistid) {
        return ResponseEntity.ok(wishlistItemService.getItemsbyWishlist(wishlistid));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteWishlistItem(@PathVariable Long id) {
        wishlistItemService.deleteWishlistItem(id);
        return ResponseEntity.noContent().build();
    }

}
