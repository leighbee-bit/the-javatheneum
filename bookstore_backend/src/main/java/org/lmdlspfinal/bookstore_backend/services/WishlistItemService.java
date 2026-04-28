package org.lmdlspfinal.bookstore_backend.services;

import org.lmdlspfinal.bookstore_backend.entities.Book;
import org.lmdlspfinal.bookstore_backend.entities.WishlistItem;
import org.lmdlspfinal.bookstore_backend.repositories.WishlistItemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class WishlistItemService {

    @Autowired
    private WishlistItemRepository wishlistItemRepository;

    @Autowired
    private org.lmdlspfinal.bookstore_backend.repositories.BookRepository bookRepository;

    public List<WishlistItem> getItemsbyWishlist(Long wishlistid) {
        return wishlistItemRepository.findByWishlistWishlistid(wishlistid);
    }

    public void deleteWishlistItem(Long id) {
        wishlistItemRepository.deleteById(id);
    }

    public WishlistItem addWishlistItem(WishlistItem wishlistItem) {
        WishlistItem saved = wishlistItemRepository.save(wishlistItem);
        // Increment wishlist count on the book
        Book book = wishlistItem.getBook();
        book.setWishlistCount(book.getWishlistCount() == null ? 1 : book.getWishlistCount() + 1);
        bookRepository.save(book);
        return saved;
    }
}
