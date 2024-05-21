package com.infybuzz.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "orders")
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "order_id")
    private Long orderId;

    @Column(name = "user_id", updatable = false)
    private Long userId;

    @Column(name = "total_cost")
    private Double totalCost;

    @Enumerated(EnumType.STRING)
    @Column(name = "order_status")
    private OrderStatus orderStatus;

    @Column(name = "order_date", updatable = false)
    private LocalDateTime orderDate;

    public Order(Double totalCost, OrderStatus orderStatus, LocalDateTime orderDate, Long userId) {
        this.totalCost = totalCost;
        this.orderStatus = orderStatus;
        this.orderDate = orderDate;
        this.userId = userId;
    }
}
