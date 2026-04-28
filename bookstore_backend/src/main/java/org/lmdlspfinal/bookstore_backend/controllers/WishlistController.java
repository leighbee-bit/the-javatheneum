package org.lmdlspfinal.bookstore_backend.controllers;


import org.lmdlspfinal.bookstore_backend.entities.Wishlist;
import org.lmdlspfinal.bookstore_backend.services.WishlistService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/wishlists")
public class WishlistController {

    @Autowired
    private WishlistService wishlistService;

    @PostMapping
    public ResponseEntity<Wishlist> createWishlist(@RequestBody Wishlist wishlist){
        return ResponseEntity.ok(wishlistService.createWishlist(wishlist));
    }

    @GetMapping("/user/{userid}")
    public ResponseEntity<List<Wishlist>> getWishlistsByUser(@PathVariable Long userid){
        return ResponseEntity.ok(wishlistService.getWishlistsByUser(userid));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Wishlist> getWishlistById(@PathVariable Long id) {
        return wishlistService.getWishlistById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteWishlist(@PathVariable Long id) {
        wishlistService.deleteWishlist(id);
        return ResponseEntity.noContent().build();
    }
}
