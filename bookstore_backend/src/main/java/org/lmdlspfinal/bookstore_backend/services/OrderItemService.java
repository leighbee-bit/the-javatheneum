package org.lmdlspfinal.bookstore_backend.services;


import org.lmdlspfinal.bookstore_backend.entities.OrderItem;
import org.lmdlspfinal.bookstore_backend.repositories.OrderItemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OrderItemService {

    @Autowired
    public OrderItemRepository orderItemRepository;

    public OrderItem addOrderItem(OrderItem orderItem) {
        return orderItemRepository.save(orderItem);
    }

    public List<OrderItem> getItemsByOrder(Long orderid) {
        return orderItemRepository.findByOrderOrderid(orderid);
    }

    public void deleteOrderItem(Long orderid) {
        orderItemRepository.deleteById(orderid);
    }

}
