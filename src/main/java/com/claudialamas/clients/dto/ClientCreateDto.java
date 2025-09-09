package com.claudialamas.clients.dto;

import javax.validation.constraints.NotBlank;

public class ClientCreateDto {
    @NotBlank
    private String name;

    @NotBlank
    private String email;

    @NotBlank
    private int vatNumber;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public int getVatNumber() {
        return vatNumber;
    }

    public void setVatNumber(int vatNumber) {
        this.vatNumber = vatNumber;
    }
}
