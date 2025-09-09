package com.claudialamas.clients.mapper;

import com.claudialamas.clients.Client;
import com.claudialamas.clients.dto.ClientCreateDto;
import com.claudialamas.clients.dto.ClientReadDto;

import java.util.List;
import java.util.stream.Collectors;

public class ClientConverter {

    public static ClientReadDto fromEntityToClientReadDto(Client client) {

        return new ClientReadDto(
                client.getId(),
                client.getName(),
                client.getEmail(),
                client.getVatNumber()
        );

    }

    public static List<ClientReadDto> fromEntityListToClientReadDtoList(List<Client> clients) {
        return clients.stream().map(ClientConverter::fromEntityToClientReadDto).collect(Collectors.toList());
    }

    public static Client fromClientCreateDtoToEntity(ClientCreateDto clientCreateDto) {

        return new Client(clientCreateDto.getName(), clientCreateDto.getEmail(), clientCreateDto.getVatNumber());
    }


}
