package com.claudialamas.ErrorLog;

import com.claudialamas.ErrorLog.dto.ErrorLogCreateDto;
import com.claudialamas.ErrorLog.dto.ErrorLogReadDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;

@Service
@Transactional
public class ErrorLogService {

    private final ErrorLogRepository errorLogRepository;
    private final ErrorLogConverter errorLogConverter;

    public ErrorLogService(ErrorLogRepository errorLogRepository, ErrorLogConverter errorLogConverter) {
        this.errorLogRepository = errorLogRepository;
        this.errorLogConverter = errorLogConverter;
    }

    public Page<ErrorLog> listAllErrors(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return errorLogRepository.findAll(pageable);
    }

    @Transactional
    public ErrorLogReadDto saveErrorLog(ErrorLogCreateDto errorLogCreateDto) {
        ErrorLog errorLog = new ErrorLog();
        errorLog.setLevel(errorLogCreateDto.getLevel());
        errorLog.setMessage(errorLogCreateDto.getMessage());
        errorLog.setExceptionType(errorLogCreateDto.getExceptionType());

        errorLog.setOccurredAt(OffsetDateTime.now(ZoneOffset.UTC));

        ErrorLog error = errorLogRepository.save(errorLog);

        return errorLogConverter.toReadDto(error);

    }
}
