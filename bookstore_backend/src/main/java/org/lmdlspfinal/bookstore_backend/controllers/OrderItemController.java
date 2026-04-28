package org.lmdlspfinal.bookstore_backend.controllers;


import org.lmdlspfinal.bookstore_backend.entities.OrderItem;
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

    @PostMapping
    public ResponseEntity<OrderItem> addOrderItem(@RequestBody OrderItem orderItem) {
        return ResponseEntity.ok(orderItemService.addOrderItem(orderItem));
    }

    @GetMapping("/order/{orderid}")
    public ResponseEntity<List<OrderItem>> getItemsbyOrder(@PathVariable Long orderid) {
        return ResponseEntity.ok(orderItemService.getItemsByOrder(orderid));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteOrderItem(@PathVariable Long id) {
        orderItemService.deleteOrderItem(id);
        return ResponseEntity.noContent().build();
    }


}
