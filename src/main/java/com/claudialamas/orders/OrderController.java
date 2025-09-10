package com.claudialamas.orders;

import com.claudialamas.exception.client.ClientDoesNotExistException;
import com.claudialamas.exception.order.OrderNotFoundException;
import com.claudialamas.orders.dto.OrderCreateDto;
import com.claudialamas.orders.dto.OrderReadDto;
import com.claudialamas.orders.dto.OrderUpdateDto;
import org.springframework.data.domain.Page;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.time.LocalDate;
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

    @GetMapping("/statusFilter")
    public Page<OrderReadDto> listOrdersByStatus(@RequestParam OrderStatus status, @RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "10") int size) {
        return orderService.listOrderByStatus(status, page, size);
    }

    @GetMapping("/dateFilter")
    public Page<OrderReadDto> listOrdersByDate(@RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
                                               @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate,
                                               @RequestParam(defaultValue = "0") int page,
                                               @RequestParam(defaultValue = "10") int size) {

        return orderService.listByDateRange(startDate, endDate, page, size);

    }






}
