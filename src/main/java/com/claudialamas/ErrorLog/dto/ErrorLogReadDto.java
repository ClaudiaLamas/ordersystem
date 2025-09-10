package com.claudialamas.ErrorLog.dto;

import com.claudialamas.ErrorLog.ErrorLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.OffsetDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ErrorLogReadDto {

    private Long id;

    private ErrorLevel level;
    private String message;

    private String exceptionType;

    private OffsetDateTime occurredAt;
}
