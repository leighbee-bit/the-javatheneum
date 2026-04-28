package org.lmdlspfinal.bookstore_backend.repositories;

import org.lmdlspfinal.bookstore_backend.entities.Book;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface BookRepository extends JpaRepository<Book, Long> {
    List<Book> findByTitleContainingIgnoreCase(String title);
    List<Book> findByAuthorContainingIgnoreCase(String author);
    List<Book> findByGenreContainingIgnoreCase(String genre);
    List<Book> findTop10ByOrderByWishlistCountDesc();
}