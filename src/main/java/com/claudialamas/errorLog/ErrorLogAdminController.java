package com.claudialamas.errorLog;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.time.ZoneId;

@RestController
@RequestMapping("/admin/error-report")
@RequiredArgsConstructor
public class ErrorLogAdminController {
    private final ErrorLogService errorLogService;

    @PostMapping("/today")
    public void sendToday() {
        errorLogService.sendDailySummary(LocalDate.now(ZoneId.of("Europe/Lisbon")));
    }


}
