package org.lmdlspfinal.bookstore_backend.entities;

import jakarta.persistence.*;
import lombok.Data;
import java.math.BigDecimal;

@Data
@Entity
@Table(name = "order_items")
public class OrderItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long orderitemid;

    @ManyToOne
    @JoinColumn(name = "orderid", nullable = false)
    private Order order;

    @ManyToOne
    @JoinColumn(name = "bookid", nullable = false)
    private Book book;

    @Column(nullable = false)
    private int quantity;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal price;
}