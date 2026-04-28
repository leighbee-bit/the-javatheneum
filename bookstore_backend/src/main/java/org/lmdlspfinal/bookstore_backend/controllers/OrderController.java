package org.lmdlspfinal.bookstore_backend.controllers;


import org.lmdlspfinal.bookstore_backend.entities.Order;
import org.lmdlspfinal.bookstore_backend.services.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/orders")
public class OrderController {

    @Autowired
    private OrderService orderService;

    @PostMapping
    public ResponseEntity<Order> createOrder(@RequestBody Order order) {
        return ResponseEntity.ok(orderService.createOrder(order));
    }

    @GetMapping("/user/{userid}")
    public ResponseEntity<List<Order>> getOrdersByUser(@PathVariable Long userid) {
        return ResponseEntity.ok(orderService.getOrdersByUser(userid));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Order> getOrderById(@PathVariable Long id) {
        return orderService.getOrderById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<Order> updateOrder(@PathVariable Long id, @RequestBody Order order)
    {
        order.setOrderid(id);
        return ResponseEntity.ok(orderService.updateOrder(order));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteOrder(@PathVariable Long id)
    {
        orderService.deleteOrder(id);
        return ResponseEntity.noContent().build();
    }

}
