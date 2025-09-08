package com.claudialamas.api.service;

import com.claudialamas.api.entity.OrderStatusHistory;
import com.claudialamas.api.repository.OrderStatusHistoryRepository;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Optional;

@Service
@Transactional
public class OrderStatusHistoryService {

    private final OrderStatusHistoryRepository orderHistoryRepository;


    public OrderStatusHistoryService(OrderStatusHistoryRepository orderHistoryRepository) {
        this.orderHistoryRepository = orderHistoryRepository;
    }

    public OrderStatusHistory listHistoryStatus(Long orderId) {

        Optional<OrderStatusHistory> orderStatusHistory = orderHistoryRepository.findById(orderId);

        return orderStatusHistory.get();

    }

}
