package com.claudialamas.validation;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class EmailValidationDto {

        private String result;
        private String details;
        private Boolean success;
        private String message;

        public boolean isValid() {
            return "valid".equalsIgnoreCase(result);
        }

}
