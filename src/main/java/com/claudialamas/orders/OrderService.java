package com.claudialamas.orders;

import com.claudialamas.errorLog.ErrorLevel;
import com.claudialamas.errorLog.ErrorLogService;
import com.claudialamas.errorLog.dto.ErrorLogCreateDto;
import com.claudialamas.clients.Client;
import com.claudialamas.clients.ClientRepository;
import com.claudialamas.clients.mapper.ClientMapper;
import com.claudialamas.exception.client.ClientDoesNotExistException;
import com.claudialamas.exception.order.OrderNotFoundException;
import com.claudialamas.orders.dto.OrderCreateDto;
import com.claudialamas.orders.dto.OrderReadDto;
import com.claudialamas.orders.dto.OrderUpdateDto;
import com.claudialamas.orders.mapper.OrderConverter;
import com.claudialamas.orders.orderStatusHistory.OrderStatusHistory;
import com.claudialamas.validation.EmailValidationClient;
import com.claudialamas.validation.EmailValidationDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import static java.util.Objects.requireNonNull;

@Service
@Transactional
public class OrderService {

    private final OrderRepository orderRepository;
    private final ClientRepository clientRepository;
    private final EmailValidationClient emailValidator;
    private final ErrorLogService errorLogService;


    public OrderService(OrderRepository orderRepository, ClientRepository clientRepository, ClientMapper clientMapper, EmailValidationClient emailValidator, ErrorLogService errorLogService) {
        this.orderRepository = orderRepository;
        this.clientRepository = clientRepository;
        this.emailValidator = emailValidator;
        this.errorLogService = errorLogService;
    }

    public List<OrderReadDto> listOrders() {
        List<Order> orders = orderRepository.findAll();
        return OrderConverter.fromEntityListToReadDto(orders);
    }

    @Transactional
    public Long addOrder(OrderCreateDto orderCreateDto) throws ClientDoesNotExistException {

        Client client = clientRepository.findById(orderCreateDto.getClientId()).orElseThrow(() -> {
            ClientDoesNotExistException ex =
                    new ClientDoesNotExistException("Client Not Found: id=" + orderCreateDto.getClientId());

            ErrorLogCreateDto dto = new ErrorLogCreateDto(ErrorLevel.WARN, ex.getMessage(), ex.getClass().getName());
            errorLogService.saveErrorLog(dto);

            return ex;
        });

        EmailValidationDto captainVerifyResponse = emailValidator.validate(client.getEmail());

        Order order = new Order();
        order.setValue(orderCreateDto.getValue());

        if (captainVerifyResponse != null && Boolean.TRUE.equals(captainVerifyResponse.getSuccess())) {
            if (captainVerifyResponse.isValid()) {
                order.setValidationResult("VALID (" + safe(captainVerifyResponse.getDetails()) + ")");
            } else {
                order.setValidationResult("INVALID (" + safe(captainVerifyResponse.getDetails()) + ")");
                order.setStatus(OrderStatus.REJEITADO);
            }
        } else {
            // SERVICE FAILED - CREDIT MISSING :)
            String msg = (captainVerifyResponse != null ? safe(captainVerifyResponse.getMessage()) : "no response");
            order.setValidationResult("VALIDATION_UNAVAILABLE (" + msg + ")");
        }

        client.addOrder(order);
        orderRepository.save(order);

        return order.getId();

    }

    private String safe(String s) {
        return s == null ? "-": s;
    }

    @Transactional
    public OrderReadDto orderUpdateStatus(Long orderId, OrderUpdateDto orderUpdateDto, String changedBy) throws OrderNotFoundException {

        Order order = orderRepository.findById(orderId).orElse(null);
        if (order == null) {
            OrderNotFoundException ex =
                    new OrderNotFoundException("order not found: id = " + orderId);
            // Logar no teu ErrorLog
            ErrorLogCreateDto dto =
                    new ErrorLogCreateDto(ErrorLevel.WARN, ex.getMessage(), ex.getClass().getName());
            errorLogService.saveErrorLog(dto);
            throw ex;
        }



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

    @Transactional
    public Page<OrderReadDto> listOrderByStatus(OrderStatus status, int page, int size) {

        if (status == null) {
            throw new IllegalArgumentException("Status needed!");
        }

        Pageable pageable = PageRequest.of(page, size);
        return orderRepository.findByStatus(requireNonNull(status), pageable).map(OrderConverter::fromEntityToOrderReadDto);

    }


    public Page<OrderReadDto> listByDateRange(LocalDate startDate, LocalDate endDate, int page, int size) {

        validateRangeDate(startDate, endDate);

        LocalDateTime start = (startDate != null) ? startDate.atStartOfDay() : null;
        LocalDateTime end   = (endDate   != null) ? endDate.plusDays(1).atStartOfDay() : null;

        Pageable pageable = PageRequest.of(page, size);

        if (start != null && end != null) {
            return orderRepository.findByCreatedAtBetween(start, end, pageable).map(OrderConverter::fromEntityToOrderReadDto);
        }

        if (start != null) {
            return orderRepository.findByCreatedAtGreaterThanEqual(start, pageable).map(OrderConverter::fromEntityToOrderReadDto);
        }

        if (end != null) {
            return orderRepository.findByCreatedAtLessThan(end, pageable).map(OrderConverter::fromEntityToOrderReadDto);
        }

        return Page.empty(pageable);
    }



    private void validateRangeDate(LocalDate startDate, LocalDate endDate) {

        if (startDate != null && endDate != null && endDate.isBefore(startDate)) {
            throw new IllegalArgumentException("End date must be after start date!");
        }

    }




}
