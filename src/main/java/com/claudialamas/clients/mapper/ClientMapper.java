package com.claudialamas.clients.mapper;

import com.claudialamas.clients.Client;
import com.claudialamas.clients.dto.ClientCreateDto;
import com.claudialamas.clients.dto.ClientReadDto;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ClientMapper {
    Client toEntity(ClientCreateDto dto);
    ClientReadDto toDto(Client entity);
}
