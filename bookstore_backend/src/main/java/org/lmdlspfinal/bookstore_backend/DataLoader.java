package org.lmdlspfinal.bookstore_backend;

import org.lmdlspfinal.bookstore_backend.services.GoogleBooksService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

//@Component
public class DataLoader implements CommandLineRunner {

    @Autowired
    private GoogleBooksService googleBooksService;

    @Autowired
    private org.lmdlspfinal.bookstore_backend.repositories.BookRepository bookRepository;

    @Override
    public void run(String... args) throws Exception {
        if (bookRepository.count() < 10) {
            System.out.println("Populating book library in background...");

            Thread populationThread = new Thread(() -> {
                try {
                    googleBooksService.populateBooks("classic literature", 20);
                    Thread.sleep(10000);
                    googleBooksService.populateBooks("science fiction", 20);
                    Thread.sleep(10000);
                    googleBooksService.populateBooks("mystery thriller", 20);
                    Thread.sleep(10000);
                    googleBooksService.populateBooks("biography", 20);
                    System.out.println("Done! Total books: " + bookRepository.count());
                } catch (Exception e) {
                    System.out.println("Population error: " + e.getMessage());
                }
            });

            populationThread.setDaemon(true);
            populationThread.start();

            System.out.println("App ready! Books populating in background...");
        }
    }

}