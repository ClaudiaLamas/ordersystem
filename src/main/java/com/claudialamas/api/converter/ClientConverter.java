package com.claudialamas.api.converter;

import com.claudialamas.api.dto.clientDto.ClientCreateDto;
import com.claudialamas.api.dto.clientDto.ClientReadDto;
import com.claudialamas.api.entity.Client;

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
