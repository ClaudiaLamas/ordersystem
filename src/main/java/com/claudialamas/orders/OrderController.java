package com.claudialamas.orders;

import com.claudialamas.orders.dto.OrderCreateDto;
import com.claudialamas.orders.dto.OrderReadDto;
import com.claudialamas.orders.dto.OrderUpdateDto;
import com.claudialamas.exception.client.ClientDoesNotExistException;
import com.claudialamas.exception.order.OrderNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/v1/orders")
@Validated
public class OrderController {
    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @GetMapping("/")
    public ResponseEntity<List<OrderReadDto>> listOrders() {
        return new ResponseEntity<>(orderService.listOrders(), HttpStatus.OK);
    }

    @PostMapping("/")
    public ResponseEntity<Long> addOrder(@Valid @RequestBody OrderCreateDto orderDto) throws ClientDoesNotExistException {

        return new ResponseEntity<>(orderService.addOrder(orderDto), HttpStatus.CREATED);

    }

    @PatchMapping("/{id}/status")
    public ResponseEntity<OrderReadDto> updateOrderStatus(@PathVariable Long id, @Valid @RequestBody OrderUpdateDto orderUpdateDto) throws OrderNotFoundException {

        return new ResponseEntity<>(orderService.orderUpdateStatus(id, orderUpdateDto, "System"), HttpStatus.OK);
    }

    @GetMapping("/{clientId}/client_orders")
    public ResponseEntity<List<OrderReadDto>> listCLientOrders(@PathVariable Long clientId) throws ClientDoesNotExistException {

        return new ResponseEntity<>(orderService.listClientOrders(clientId), HttpStatus.OK);
    }




}
