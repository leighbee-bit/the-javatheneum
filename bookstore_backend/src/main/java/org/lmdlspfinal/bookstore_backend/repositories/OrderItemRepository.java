package org.lmdlspfinal.bookstore_backend.repositories;

import org.lmdlspfinal.bookstore_backend.entities.OrderItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.List;

public interface OrderItemRepository extends JpaRepository<OrderItem, Long> {

    @Query("SELECT oi FROM OrderItem oi WHERE oi.order.orderid = :orderid")
    List<OrderItem> findByOrderOrderid(@Param("orderid") Long orderid);
}