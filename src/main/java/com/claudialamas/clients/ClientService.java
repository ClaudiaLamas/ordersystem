package com.claudialamas.clients;

import com.claudialamas.ErrorLog.ErrorLogRepository;
import com.claudialamas.clients.dto.ClientCreateDto;
import com.claudialamas.clients.dto.ClientReadDto;
import com.claudialamas.clients.mapper.ClientConverter;
import com.claudialamas.clients.mapper.ClientMapper;
import com.claudialamas.exception.client.EmailAlreadyExistsException;
import com.claudialamas.exception.client.EmailNotFoundException;
import com.claudialamas.exception.client.VatNumberAlreadyExistsException;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class ClientService {

    private final ClientRepository clientRepository;
    private final ErrorLogRepository errorLogRepository;

    public ClientService(ClientRepository clientRepository, ClientMapper clientMapper, ErrorLogRepository errorLogRepository) {
        this.clientRepository = clientRepository;
        this.errorLogRepository = errorLogRepository;
    }

    public List<ClientReadDto> listClients() {
        List<Client> clients = clientRepository.findAll();
        return ClientConverter.fromEntityListToClientReadDtoList(clients);
    }


    public Long addClient(ClientCreateDto clientCreateDto) throws EmailAlreadyExistsException, VatNumberAlreadyExistsException {

        Optional<Client> clientOptional = this.clientRepository.findByEmail(clientCreateDto.getEmail());
        Optional<Client> clientOptional2 = this.clientRepository.findByVatNumber(clientCreateDto.getVatNumber());

        if (clientOptional.isPresent()) {

            throw new EmailAlreadyExistsException("this email is already associated with a client");
        }
        if (clientOptional2.isPresent()) {
            throw new VatNumberAlreadyExistsException("vat number already exists");
        }

        //Client newClient = clientMapper.toEntity(clientCreateDto);
        Client newClient = ClientConverter.fromClientCreateDtoToEntity(clientCreateDto);
        clientRepository.save(newClient);

        return newClient.getId();

    }

    public ClientReadDto findClientByEmail(String email) throws EmailNotFoundException {
        Optional<Client> client = this.clientRepository.findByEmail(email);

        if (!client.isPresent()) {
            throw new EmailNotFoundException("Email not found");
        }

        return ClientConverter.fromEntityToClientReadDto(client.get());
    }



}
