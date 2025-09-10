package com.claudialamas.orders;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;

public interface OrderRepository extends JpaRepository<Order, Long> {

    Page<Order> findByStatus(OrderStatus status, Pageable pageable);
    Page<Order> findByCreatedAtBetween(LocalDateTime start, LocalDateTime end, Pageable pageable);
    Page<Order> findByCreatedAtGreaterThanEqual(LocalDateTime start, Pageable pageable);
    Page<Order> findByCreatedAtLessThan(LocalDateTime end, Pageable pageable);


}
