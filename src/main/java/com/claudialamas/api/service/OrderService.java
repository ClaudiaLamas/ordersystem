package com.claudialamas.api.service;

import com.claudialamas.api.converter.OrderConverter;
import com.claudialamas.api.dto.orderDto.OrderCreateDto;
import com.claudialamas.api.dto.orderDto.OrderReadDto;
import com.claudialamas.api.dto.orderDto.OrderUpdateDto;
import com.claudialamas.api.entity.Client;
import com.claudialamas.api.entity.Order;
import com.claudialamas.api.entity.OrderStatus;
import com.claudialamas.api.entity.OrderStatusHistory;
import com.claudialamas.api.repository.ClientRepository;
import com.claudialamas.api.repository.OrderRepository;
import com.claudialamas.exception.client.ClientDoesNotExixtException;
import com.claudialamas.exception.order.OrderNotFoundException;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class OrderService {

    private final OrderRepository orderRepository;
    private final ClientRepository clientRepository;


    public OrderService(OrderRepository orderRepository, ClientRepository clientRepository) {
        this.orderRepository = orderRepository;
        this.clientRepository = clientRepository;
    }

    public List<OrderReadDto> listOrders() {
        List<Order> orders = orderRepository.findAll();
        return OrderConverter.fromEntityListToReadDto(orders);
    }

    @Transactional
    public Long addOrder(OrderCreateDto orderCreateDto) throws ClientDoesNotExixtException {

        Client client = clientRepository.findById(orderCreateDto.getClientId()).orElseThrow(() -> new ClientDoesNotExixtException("Client Not Found"));


        Order order = new Order();
        order.setValue(orderCreateDto.getValue());
        client.addOrder(order);

        orderRepository.save(order);

        return order.getId();

    }

    @Transactional
    public OrderReadDto orderUpdateStatus(Long orderId, OrderUpdateDto orderUpdateDto, String changedBy) throws OrderNotFoundException {

        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new OrderNotFoundException("order not found: id = " + orderId));


        OrderStatus old = order.getStatus();
        OrderStatus newStatus = orderUpdateDto.getNewOrderStatus();

        if (old == newStatus) {
            return OrderConverter.fromEntityToOrderReadDto(order);
        }

        OrderStatusHistory newHistory = new OrderStatusHistory();
        newHistory.setOldStatus(old);
        newHistory.setNewStatus(newStatus);
        newHistory.setChangedBy(changedBy);

        order.addHistory(newHistory);
        order.setStatus(newStatus);

        orderRepository.save(order);

        return OrderConverter.fromEntityToOrderReadDto(order);

    }

    @Transactional
    public List<OrderReadDto> listClientOrders(Long clientId) throws ClientDoesNotExixtException {

        Client client = clientRepository.findById(clientId).orElseThrow(() -> new ClientDoesNotExixtException("Cleint not found!"));

        return client.getOrders().stream().map(OrderConverter::fromEntityToOrderReadDto).collect(Collectors.toList());
    }
}
