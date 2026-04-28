package org.lmdlspfinal.bookstore_backend.services;

import org.lmdlspfinal.bookstore_backend.entities.Order;
import org.lmdlspfinal.bookstore_backend.repositories.BookRepository;
import org.lmdlspfinal.bookstore_backend.repositories.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class OrderService {


    @Autowired
    private OrderRepository orderRepository;

    public Order createOrder(Order order) {
        return orderRepository.save(order);
    }

    public Order updateOrder(Order order) {
        return orderRepository.save(order);
    }

    public List<Order> getOrdersByUser(Long userid) {
        return orderRepository.findByUserUserid(userid);
    }

    public Optional<Order> getOrderById(Long id) {
        return orderRepository.findById(id);
    }

    public void deleteOrder(Long id) {
        orderRepository.deleteById(id);
    }

}
