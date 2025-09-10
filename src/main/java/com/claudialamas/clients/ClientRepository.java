package com.claudialamas.clients;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ClientRepository extends JpaRepository<Client, Long> {
    Optional<Client> findByEmail(String email);
    boolean existsByEmail(String email);

    Optional<Client> findByVatNumber(int vatNumber);
    boolean existsByEmail(int vatNumber);
}
