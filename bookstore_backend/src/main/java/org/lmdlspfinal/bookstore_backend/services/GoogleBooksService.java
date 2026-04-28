package org.lmdlspfinal.bookstore_backend.services;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.lmdlspfinal.bookstore_backend.entities.Book;
import org.lmdlspfinal.bookstore_backend.repositories.BookRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Random;

@Service
public class GoogleBooksService {

    @Autowired
    private BookRepository bookRepository;

    @Value("${google.books.api.key}")
    private String apiKey;

    private final WebClient webClient = WebClient.create("https://www.googleapis.com/books/v1");
    private final ObjectMapper mapper = new ObjectMapper();

    public void populateBooks(String query, int maxResults) {
        try {
            String response = webClient.get()
                    .uri(uriBuilder -> uriBuilder
                            .path("/volumes")
                            .queryParam("q", query)
                            .queryParam("maxResults", maxResults)
                            .queryParam("printType", "books")
                            .queryParam("key", apiKey)
                            .build())
                    .retrieve()
                    .bodyToMono(String.class)
                    .block();

            if (response == null) return;

            JsonNode root = mapper.readTree(response);
            if (!root.has("items")) return;

            for (JsonNode item : root.get("items")) {
                JsonNode volumeInfo = item.get("volumeInfo");
                try {
                    String title = volumeInfo.has("title") ?
                            volumeInfo.get("title").asText() : null;
                    if (title == null) continue;
                    if (!bookRepository.findByTitleContainingIgnoreCase(title).isEmpty()) continue;

                    Book book = new Book();
                    book.setTitle(title);

                    book.setAuthor(volumeInfo.has("authors") ?
                            volumeInfo.get("authors").get(0).asText() : "Unknown Author");

                    book.setDescription(volumeInfo.has("description") ?
                            volumeInfo.get("description").asText() : "No description available.");

                    Random random = new Random();
                    double price = 5.99 + (29.99 - 5.99) * random.nextDouble();
                    book.setPrice(BigDecimal.valueOf(price).setScale(2, RoundingMode.HALF_UP));

                    if (volumeInfo.has("imageLinks")) {
                        JsonNode imageLinks = volumeInfo.get("imageLinks");
                        if (imageLinks.has("thumbnail")) {
                            book.setImageUrl(imageLinks.get("thumbnail").asText()
                                    .replace("http://", "https://"));
                        }
                    }

                    if (volumeInfo.has("pageCount")) {
                        book.setPageCount(volumeInfo.get("pageCount").asInt());
                    }

                    if (volumeInfo.has("previewLink")) {
                        book.setPreviewLink(volumeInfo.get("previewLink").asText());
                    }

                    if (volumeInfo.has("categories")) {
                        book.setGenre(volumeInfo.get("categories").get(0).asText());
                    } else {
                        book.setGenre(query.split(" ")[0]); // fallback to query keyword
                    }

                    bookRepository.save(book);
                    System.out.println("Saved: " + title);
                } catch (Exception e) {
                    System.out.println("Skipping book: " + e.getMessage());
                }
            }
        } catch (Exception e) {
            System.out.println("Error for query '" + query + "': " + e.getMessage());
        }
    }
}