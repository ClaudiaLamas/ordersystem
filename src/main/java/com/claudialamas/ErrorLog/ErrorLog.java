package com.claudialamas.ErrorLog;

import lombok.Data;

import javax.persistence.*;
import java.time.OffsetDateTime;

@Data
@Entity
@Table(name = "error_log")
public class ErrorLog {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "level", nullable = false, length = 32)
    private ErrorLevel level;

    @Column(name = "message", nullable = false, length = 2000)
    private String message;

    @Column(name = "exception_type", length = 255)
    private String exceptionType;

    @Column(name = "occurred_at", nullable = false, columnDefinition = "datetimeoffset(3)")
    private OffsetDateTime occurredAt;
}
