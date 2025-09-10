package com.claudialamas.errorLog;

import com.claudialamas.errorLog.dto.ErrorLogCreateDto;
import com.claudialamas.errorLog.dto.ErrorLogReadDto;
import org.springframework.stereotype.Component;

@Component
public class ErrorLogConverter {

    public ErrorLog fromCreateDtoToEntity(ErrorLogCreateDto dto) {
         ErrorLog e = new ErrorLog();
         e.setLevel(dto.getLevel());
         e.setMessage(dto.getMessage());
         e.setExceptionType(dto.getExceptionType());
         return e;
    }

    public ErrorLogReadDto toReadDto(ErrorLog e) {
        if (e == null) return null;
        ErrorLogReadDto dto = new ErrorLogReadDto();
        dto.setId(e.getId());
        dto.setLevel(e.getLevel());
        dto.setMessage(e.getMessage());
        dto.setExceptionType(e.getExceptionType());
        dto.setOccurredAt(e.getOccurredAt());
        return dto;
    }

}
