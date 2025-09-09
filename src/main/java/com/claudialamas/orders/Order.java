package com.claudialamas.orders;

import com.claudialamas.clients.Client;
import com.claudialamas.orders.orderStatusHistory.OrderStatusHistory;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "orders")
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "client_id", nullable = false)
    private Client client;

    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private OrderStatus status = OrderStatus.PENDENTE;

    @Column(nullable = false, precision = 12, scale = 2)
    private BigDecimal value;

    @Column
    private String validationResult;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<OrderStatusHistory> history = new ArrayList<>();

    public Long getId() {
        return id;
    }

    public Order() {
    }

    public Order(Client client, BigDecimal value) {
        this.client = client;
        this.value = value;
    }

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
        if (this.status == null) {
            this.status = OrderStatus.PENDENTE;
        }
    }

    public Client getClient() {
        return client;
    }

    public void setClient(Client client) {
        this.client = client;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }


    public OrderStatus getStatus() {
        return status;
    }

    public void setStatus(OrderStatus status) {
        this.status = status;
    }

    public BigDecimal getValue() {
        return value;
    }

    public void setValue(BigDecimal value) {
        this.value = value;
    }

    public String getValidationResult() {
        return validationResult;
    }

    public void setValidationResult(String validationResult) {
        this.validationResult = validationResult;
    }

    public List<OrderStatusHistory> getHistory() {
        return history;
    }

    public void setHistory(List<OrderStatusHistory> history) {
        this.history = history;
    }

    public void addHistory(OrderStatusHistory newHistory) {
        history.add(newHistory);
        newHistory.setOrder(this);
    }
}
