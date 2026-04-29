package org.lmdlspfinal.bookstore_backend.controllers;

import org.lmdlspfinal.bookstore_backend.entities.Order;
import org.lmdlspfinal.bookstore_backend.entities.OrderItem;
import org.lmdlspfinal.bookstore_backend.repositories.OrderRepository;
import org.lmdlspfinal.bookstore_backend.services.OrderItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/order-items")
public class OrderItemController {

    @Autowired
    private OrderItemService orderItemService;

    @Autowired
    private OrderRepository orderRepository;


    @GetMapping("/order/{orderid}")
    public ResponseEntity<List<OrderItem>> getItemsByOrder(@PathVariable Long orderid) {
        return ResponseEntity.ok(orderItemService.getItemsByOrder(orderid));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteOrderItem(@PathVariable Long id) {
        orderItemService.deleteOrderItem(id);
        return ResponseEntity.noContent().build();
    }

    @Autowired
    private org.lmdlspfinal.bookstore_backend.repositories.BookRepository bookRepository;

    @PostMapping
    public ResponseEntity<OrderItem> addOrderItem(@RequestBody OrderItem orderItem) {
        if (orderItem.getOrder() != null && orderItem.getOrder().getOrderid() != null) {
            Order order = orderRepository.findById(orderItem.getOrder().getOrderid())
                    .orElseThrow(() -> new RuntimeException("Order not found"));
            orderItem.setOrder(order);
        }

        if (orderItem.getBook() != null && orderItem.getBook().getBookid() != null) {
            org.lmdlspfinal.bookstore_backend.entities.Book book = bookRepository.findById(orderItem.getBook().getBookid())
                    .orElseThrow(() -> new RuntimeException("Book not found"));
            orderItem.setBook(book);
        }

        return ResponseEntity.ok(orderItemService.addOrderItem(orderItem));
    }
}