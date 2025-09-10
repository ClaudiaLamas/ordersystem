package com.claudialamas.errorLog.dto;

import com.claudialamas.errorLog.ErrorLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ErrorLogCreateDto {

    @NotBlank
    private ErrorLevel level;

    @NotBlank
    private String message;

    private String exceptionType;
}