package com.claudialamas.api.service;

import com.claudialamas.api.converter.OrderConverter;
import com.claudialamas.api.dto.orderDto.OrderCreateDto;
import com.claudialamas.api.dto.orderDto.OrderReadDto;
import com.claudialamas.api.dto.orderDto.OrderUpdateDto;
import com.claudialamas.api.entity.Client;
import com.claudialamas.api.entity.Order;
import com.claudialamas.api.repository.ClientRepository;
import com.claudialamas.api.repository.OrderRepository;
import com.claudialamas.exception.client.ClientDoesNotExixtException;
import com.claudialamas.exception.order.OrderNotFoundException;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

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

        Optional<Client> client = clientRepository.findById(orderCreateDto.getClientId());

        if (!client.isPresent()) {
            throw new ClientDoesNotExixtException("O cliente não existe!");
        }

        Order order = new Order();
        order.setClient(client.get());
        order.setValue(orderCreateDto.getValue());

        orderRepository.save(order);

        return order.getId();

    }

    @Transactional
    public Long orderUpdateStatus(OrderUpdateDto orderUpdateDto) throws OrderNotFoundException {

        Optional<Order> orderOptional = orderRepository.findById(orderUpdateDto.getOrderId());

        if (!orderOptional.isPresent()) {
            throw new OrderNotFoundException("Order not found!");
        }

        orderOptional.get().setStatus(orderUpdateDto.getNewOrderStatus());

        return orderOptional.get().getId();

    }
}
