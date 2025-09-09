package com.claudialamas.orders.mapper;

import com.claudialamas.orders.dto.OrderCreateDto;
import com.claudialamas.orders.dto.OrderReadDto;
import com.claudialamas.clients.Client;
import com.claudialamas.orders.Order;
import com.claudialamas.clients.ClientRepository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class OrderConverter {

    private ClientRepository clientRepository;

    public OrderConverter(ClientRepository clientRepository) {
        this.clientRepository = clientRepository;
    }


    public Order fromOrderCreateDtoToEntity(OrderCreateDto orderCreateDto) {

        Optional<Client> clientOptional = clientRepository.findById(orderCreateDto.getClientId());

        return new Order( clientOptional.get(), orderCreateDto.getValue());

    }

    public static OrderReadDto fromEntityToOrderReadDto(Order order) {
        return new OrderReadDto(order.getId(), order.getClient().getName(), order.getClient().getEmail(), order.getCreatedAt(), order.getStatus(), order.getValue(), order.getValidationResult());
    }

    public static List<OrderReadDto> fromEntityListToReadDto(List<Order> orders) {
        return orders.stream().map(OrderConverter::fromEntityToOrderReadDto).collect(Collectors.toList());
    }
}
