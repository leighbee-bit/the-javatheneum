package org.lmdlspfinal.bookstore_backend.services;

import org.lmdlspfinal.bookstore_backend.entities.Book;
import org.lmdlspfinal.bookstore_backend.repositories.BookRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class BookService {

    @Autowired
    private BookRepository bookRepository;

    public Book addBook(Book book) {
        return bookRepository.save(book);
    }

    public List<Book> getAllBooks() {
        return bookRepository.findAll();
    }

    public Optional<Book> getBookById(Long id) {
        return bookRepository.findById(id);
    }

    public List<Book> searchByTitle(String title) {
        return bookRepository.findByTitleContainingIgnoreCase(title);
    }

    public List<Book> searchByAuthor(String author) {
        return bookRepository.findByAuthorContainingIgnoreCase(author);
    }

    public Book updateBook(Book book) {
        return bookRepository.save(book);
    }

    public void deleteBookById(Long id) {
        bookRepository.deleteById(id);
    }

    public List<Book> getBooksByGenre(String genre) {
        return bookRepository.findByGenreContainingIgnoreCase(genre);
    }

    public List<Book> getPopularBooks() {
        return bookRepository.findTop10ByOrderByWishlistCountDesc();
    }
}
