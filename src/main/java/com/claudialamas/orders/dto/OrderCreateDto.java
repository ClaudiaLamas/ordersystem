package com.claudialamas.orders.dto;

import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.Digits;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

public class OrderCreateDto {
    @NotNull
    private Long clientId;
    @NotNull @DecimalMin("0.01") @Digits(integer = 12, fraction = 2)
    private BigDecimal value;

    public Long getClientId() {
        return clientId;
    }

    public void setClientId(Long clientId) {
        this.clientId = clientId;
    }

    public BigDecimal getValue() {
        return value;
    }

    public void setValue(BigDecimal value) {
        this.value = value;
    }
}
