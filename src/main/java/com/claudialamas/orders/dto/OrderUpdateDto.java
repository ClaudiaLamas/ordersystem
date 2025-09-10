package com.claudialamas.orders.dto;

import com.claudialamas.orders.OrderStatus;

import javax.validation.constraints.NotNull;

public class OrderUpdateDto {

    @NotNull
    private OrderStatus newOrderStatus;


    public OrderStatus getNewOrderStatus() {
        return newOrderStatus;
    }

    public void setNewOrderStatus(OrderStatus newOrderStatus) {
        this.newOrderStatus = newOrderStatus;
    }
}
