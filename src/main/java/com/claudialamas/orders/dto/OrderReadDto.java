package com.claudialamas.orders.dto;

import com.claudialamas.orders.OrderStatus;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class OrderReadDto {

    private Long id;
    private String clientName;
    private String clientEmail;
    private LocalDateTime createdAt;
    private OrderStatus orderStatus;
    private BigDecimal value;
    private String validationResult;

    public OrderReadDto(Long id, String clientName, String clientEmail, LocalDateTime createdAt, OrderStatus orderStatus, BigDecimal value, String validationResult) {
        this.id = id;
        this.clientName = clientName;
        this.clientEmail = clientEmail;
        this.createdAt = createdAt;
        this.orderStatus = orderStatus;
        this.value = value;
        this.validationResult = validationResult;
    }

    public Long getId() {
        return id;
    }

    public String getClientName() {
        return clientName;
    }

    public String getClientEmail() {
        return clientEmail;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public OrderStatus getOrderStatus() {
        return orderStatus;
    }

    public BigDecimal getValue() {
        return value;
    }

    public String getValidationResult() {
        return validationResult;
    }
}
