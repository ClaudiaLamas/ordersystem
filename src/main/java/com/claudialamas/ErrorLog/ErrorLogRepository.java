package com.claudialamas.ErrorLog;

import com.claudialamas.orders.Order;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;

public interface ErrorLogRepository extends JpaRepository<ErrorLog, Long> {

    Page<Order> findByOccurredAtBetween(LocalDateTime start, LocalDateTime end, Pageable pageable);
}
