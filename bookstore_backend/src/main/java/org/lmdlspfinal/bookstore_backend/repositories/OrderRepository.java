package org.lmdlspfinal.bookstore_backend.repositories;

import org.lmdlspfinal.bookstore_backend.entities.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface OrderRepository extends JpaRepository<Order, Long> {
    List<Order> findByUserUserid(Long userid);
}