package com.claudialamas.api.controller;

import com.claudialamas.api.dto.clientDto.ClientCreateDto;
import com.claudialamas.api.dto.clientDto.ClientReadDto;
import com.claudialamas.api.service.ClientService;
import com.claudialamas.exception.client.EmailAlreadyExistsException;
import com.claudialamas.exception.client.EmailNotFoundException;
import com.claudialamas.exception.client.VatNumberAlreadyExistsException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/v1/clients")
@Validated
public class ClientController {
    private final ClientService clientService;

    public ClientController(ClientService clientService) {
        this.clientService = clientService;
    }

    @GetMapping("/")
    public ResponseEntity<List<ClientReadDto>> listClients() {
        return new ResponseEntity<>(clientService.listClients(), HttpStatus.OK);
    }

    @PostMapping("/")
    public ResponseEntity<Long> addClient(@Valid @RequestBody ClientCreateDto clientCreateDto) throws EmailAlreadyExistsException, VatNumberAlreadyExistsException {
        return new ResponseEntity<>(clientService.addClient(clientCreateDto), HttpStatus.CREATED);

    }

    @GetMapping("/email")
    public ResponseEntity<ClientReadDto> findClientByEmail(@RequestParam String email) throws EmailNotFoundException {
        return new ResponseEntity<>(clientService.findClientByEmail(email), HttpStatus.OK);
    }

}
