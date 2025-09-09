package com.claudialamas.orders;

import com.claudialamas.orders.mapper.OrderConverter;
import com.claudialamas.orders.dto.OrderCreateDto;
import com.claudialamas.orders.dto.OrderReadDto;
import com.claudialamas.orders.dto.OrderUpdateDto;
import com.claudialamas.clients.Client;
import com.claudialamas.clients.mapper.ClientMapper;
import com.claudialamas.clients.ClientRepository;
import com.claudialamas.exception.client.ClientDoesNotExistException;
import com.claudialamas.exception.order.OrderNotFoundException;
import com.claudialamas.orders.orderStatusHistory.OrderStatusHistory;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class OrderService {

    private final OrderRepository orderRepository;
    private final ClientRepository clientRepository;


    public OrderService(OrderRepository orderRepository, ClientRepository clientRepository, ClientMapper clientMapper) {
        this.orderRepository = orderRepository;
        this.clientRepository = clientRepository;
    }

    public List<OrderReadDto> listOrders() {
        List<Order> orders = orderRepository.findAll();
        return OrderConverter.fromEntityListToReadDto(orders);
    }

    @Transactional
    public Long addOrder(OrderCreateDto orderCreateDto) throws ClientDoesNotExistException {

        Client client = clientRepository.findById(orderCreateDto.getClientId()).orElseThrow(() -> new ClientDoesNotExistException("Client Not Found"));


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
    public List<OrderReadDto> listClientOrders(Long clientId) throws ClientDoesNotExistException {

        Client client = clientRepository.findById(clientId).orElseThrow(() -> new ClientDoesNotExistException("Cleint not found!"));

        return client.getOrders().stream().map(OrderConverter::fromEntityToOrderReadDto).collect(Collectors.toList());
    }


}
