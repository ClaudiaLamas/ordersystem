package com.claudialamas.api.converter;

import com.claudialamas.api.dto.orderDto.OrderCreateDto;
import com.claudialamas.api.dto.orderDto.OrderReadDto;
import com.claudialamas.api.entity.Client;
import com.claudialamas.api.entity.Order;
import com.claudialamas.api.repository.ClientRepository;

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
