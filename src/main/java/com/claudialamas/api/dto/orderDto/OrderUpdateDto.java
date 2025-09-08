package com.claudialamas.api.dto.orderDto;

import com.claudialamas.api.entity.OrderStatus;

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
