package com.claudialamas.api.entity;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "order_status_history")
public class OrderStatusHistory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "order_id", nullable = false)
    private Order order;

    @Enumerated(EnumType.STRING)
    private OrderStatus oldStatus;
    @Enumerated(EnumType.STRING)
    private OrderStatus newStatus;

    private LocalDateTime changedAt= LocalDateTime.now();

    private String changedBy;

    public OrderStatusHistory() {
    }

    public OrderStatusHistory(Order order, OrderStatus oldStatus, OrderStatus newStatus, LocalDateTime changedAt, String changedBy) {
        this.order = order;
        this.oldStatus = oldStatus;
        this.newStatus = newStatus;
        this.changedAt = changedAt;
        this.changedBy = changedBy;
    }

    public Long getId() {
        return id;
    }

    public Order getOrder() {
        return order;
    }

    public void setOrder(Order order) {
        this.order = order;
    }

    public OrderStatus getOldStatus() {
        return oldStatus;
    }

    public void setOldStatus(OrderStatus oldStatus) {
        this.oldStatus = oldStatus;
    }

    public OrderStatus getNewStatus() {
        return newStatus;
    }

    public void setNewStatus(OrderStatus newStatus) {
        this.newStatus = newStatus;
    }

    public LocalDateTime getChangedAt() {
        return changedAt;
    }

    public String getChangedBy() {
        return changedBy;
    }

    public void setChangedBy(String changedBy) {
        this.changedBy = changedBy;
    }
}
