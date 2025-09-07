package com.claudialamas.api.dto.orderDto;

import com.claudialamas.api.entity.OrderStatus;

import javax.validation.constraints.NotNull;

public class OrderUpdateDto {
    @NotNull
    private Long orderId;
    @NotNull
    private OrderStatus newOrderStatus;

    public Long getOrderId() {
        return orderId;
    }

    public void setOrderId(Long orderId) {
        this.orderId = orderId;
    }

    public OrderStatus getNewOrderStatus() {
        return newOrderStatus;
    }

    public void setNewOrderStatus(OrderStatus newOrderStatus) {
        this.newOrderStatus = newOrderStatus;
    }
}
