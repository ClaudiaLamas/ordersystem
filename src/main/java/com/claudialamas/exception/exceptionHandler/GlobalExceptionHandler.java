package com.claudialamas.exception.exceptionHandler;

import com.claudialamas.exception.client.ClientDoesNotExistException;
import com.claudialamas.exception.client.EmailAlreadyExistsException;
import com.claudialamas.exception.client.EmailNotFoundException;
import com.claudialamas.exception.client.VatNumberAlreadyExistsException;
import com.claudialamas.exception.order.OrderNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.servlet.http.HttpServletRequest;
import java.time.Instant;

@RestControllerAdvice
public class GlobalExceptionHandler {

    public static class ApiError {
        private Instant timestamp;
        private int status;
        private String error;
        private String message;
        private String path;

        public ApiError(Instant timestamp, int status, String error, String message, String path) {
            this.timestamp = timestamp;
            this.status = status;
            this.error = error;
            this.message = message;
            this.path = path;
        }

        public Instant getTimestamp() { return timestamp; }
        public int getStatus() { return status; }
        public String getError() { return error; }
        public String getMessage() { return message; }
        public String getPath() { return path; }
    }

    @ExceptionHandler({ ClientDoesNotExistException.class, OrderNotFoundException.class })
    public ResponseEntity<ApiError> handleNotFound(Exception ex, HttpServletRequest req) {
        HttpStatus status = HttpStatus.NOT_FOUND;
        ApiError body = new ApiError(
                Instant.now(),
                status.value(),
                status.getReasonPhrase(),
                ex.getMessage(),
                req.getRequestURI()
        );
        return ResponseEntity.status(status).body(body);
    }

    @ExceptionHandler({
            EmailAlreadyExistsException.class,
            EmailNotFoundException.class,
            VatNumberAlreadyExistsException.class
    })
    public ResponseEntity<ApiError> handleBadRequest(Exception ex, HttpServletRequest req) {
        HttpStatus status = HttpStatus.BAD_REQUEST;
        ApiError body = new ApiError(
                Instant.now(),
                status.value(),
                status.getReasonPhrase(),
                ex.getMessage(),
                req.getRequestURI()
        );
        return ResponseEntity.status(status).body(body);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ApiError> handleIllegalArgument(IllegalArgumentException ex, HttpServletRequest req) {
        HttpStatus status = HttpStatus.BAD_REQUEST;
        ApiError body = new ApiError(
                Instant.now(),
                status.value(),
                status.getReasonPhrase(),
                ex.getMessage(),
                req.getRequestURI()
        );
        return ResponseEntity.status(status).body(body);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiError> handleUnexpected(Exception ex, HttpServletRequest req) {
        ex.printStackTrace(); // substitui por logger em produção
        HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR;
        ApiError body = new ApiError(
                Instant.now(),
                status.value(),
                status.getReasonPhrase(),
                "An unexpected error occurred. God only knows why :)",
                req.getRequestURI()
        );
        return ResponseEntity.status(status).body(body);
    }
}
