package com.claudialamas.clients.dto;

import java.time.LocalDateTime;

public class ClientReadDto {

    private Long id;
    private String name;
    private String email;
    private int vatNumber;
    private LocalDateTime createdAt;

    public ClientReadDto(Long id, String name, String email, int vatNumber) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.vatNumber = vatNumber;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public int getVatNumber() {
        return vatNumber;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
}
