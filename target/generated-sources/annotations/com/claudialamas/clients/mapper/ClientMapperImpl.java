package com.claudialamas.clients.mapper;

import com.claudialamas.clients.Client;
import com.claudialamas.clients.dto.ClientCreateDto;
import com.claudialamas.clients.dto.ClientReadDto;
import javax.annotation.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2025-09-10T16:14:26+0100",
    comments = "version: 1.5.5.Final, compiler: javac, environment: Java 1.8.0_392 (Temurin)"
)
@Component
public class ClientMapperImpl implements ClientMapper {

    @Override
    public Client toEntity(ClientCreateDto dto) {
        if ( dto == null ) {
            return null;
        }

        Client client = new Client();

        return client;
    }

    @Override
    public ClientReadDto toDto(Client entity) {
        if ( entity == null ) {
            return null;
        }

        Long id = null;
        String name = null;
        String email = null;
        int vatNumber = 0;

        ClientReadDto clientReadDto = new ClientReadDto( id, name, email, vatNumber );

        return clientReadDto;
    }
}
