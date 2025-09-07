package com.claudialamas.api.entity;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "Clients")
public class Client {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false)
    private String name;
    @Column(nullable = false, unique = true)
    private String email;
    @Column(nullable = false, unique = true)
    private int vatNumber;
    private LocalDateTime createdAt = LocalDateTime.now();

    public Client() {
    }

    public Client(String name, String email, int vatNumber) {
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

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

}
