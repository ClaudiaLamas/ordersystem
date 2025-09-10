package com.claudialamas.errorLog;

import com.claudialamas.errorLog.dto.ErrorLogCreateDto;
import com.claudialamas.errorLog.dto.ErrorLogReadDto;
import com.claudialamas.email.EmailService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Service
@Transactional
@Slf4j
public class ErrorLogService {

    private final ErrorLogRepository errorLogRepository;
    private final ErrorLogConverter errorLogConverter;
    private final EmailService emailService;

    @Value("${app.errorlog.report.recipient:no-reply@example.com}")
    private String reportRecipient;

    private static final ZoneId REPORT_ZONE = ZoneId.of("Europe/Lisbon");
    private static final DateTimeFormatter DATE_FMT = DateTimeFormatter.ISO_LOCAL_DATE;
    private static final DateTimeFormatter TS_FMT = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public ErrorLogService(ErrorLogRepository errorLogRepository, ErrorLogConverter errorLogConverter, EmailService emailService) {
        this.errorLogRepository = errorLogRepository;
        this.errorLogConverter = errorLogConverter;
        this.emailService = emailService;
    }

    public Page<ErrorLog> listAllErrors(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return errorLogRepository.findAll(pageable);
    }

    @Transactional
    public ErrorLogReadDto saveErrorLog(ErrorLogCreateDto errorLogCreateDto) {
        ErrorLog entity = errorLogConverter.fromCreateDtoToEntity(errorLogCreateDto);
        // Guardar sempre em UTC
        entity.setOccurredAt(OffsetDateTime.now(ZoneOffset.UTC));
        ErrorLog saved = errorLogRepository.save(entity);
        return errorLogConverter.toReadDto(saved);

    }

    public void sendDailySummary(LocalDate day) {
        OffsetDateTime startUtc = toUtcStart(day);
        OffsetDateTime endUtc   = toUtcExclusiveEnd(day);

        List<Object[]> counts = errorLogRepository.countByLevelBetween(startUtc, endUtc);
        Map<String, Long> byLevel = new LinkedHashMap<String, Long>();
        long total = 0L;
        if (counts != null) {
            for (Object[] row : counts) {
                String level = String.valueOf(row[0]);
                long c = ((Number) row[1]).longValue();
                byLevel.put(level, c);
                total += c;
            }
        }

        List<Object[]> topExc = errorLogRepository
                .topExceptionTypesBetween(startUtc, endUtc, ErrorLevel.ERROR);
        Pageable topN = PageRequest.of(0, 10);
        List<ErrorLog> latest = errorLogRepository.findByOccurredAtBetweenOrderByOccurredAtDesc(startUtc, endUtc, topN);

        String subject = "Resumo diário de erros — " + day.format(DATE_FMT);
        String html = buildEmailHtml(day, total, byLevel, topExc, latest);

        emailService.sendHtml(reportRecipient, subject, html);
        log.info("Resumo diário enviado para {} ({} eventos).", reportRecipient, total);
    }

    private OffsetDateTime toUtcStart(LocalDate date) {
        ZonedDateTime z = date.atStartOfDay(REPORT_ZONE);
        return z.toInstant().atOffset(ZoneOffset.UTC);
    }

    private OffsetDateTime toUtcExclusiveEnd(LocalDate date) {
        LocalDate next = date.plusDays(1);
        ZonedDateTime z = next.atStartOfDay(REPORT_ZONE);
        return z.toInstant().atOffset(ZoneOffset.UTC);
    }

    private String buildEmailHtml(LocalDate day,
                                  long total,
                                  Map<String, Long> byLevel,
                                  List<Object[]> topExc,
                                  List<ErrorLog> latest) {

        StringBuilder sb = new StringBuilder(4096);
        sb.append("<html><body style='font-family:Arial,Helvetica,sans-serif'>");
        sb.append("<h2>Resumo diário de erros</h2>");
        sb.append("<p><b>Dia (Europe/Lisbon):</b> ").append(day.format(DATE_FMT)).append("</p>");

        // Totais por nível
        sb.append("<h3>Totais por nível</h3>");
        if (byLevel.isEmpty()) {
            sb.append("<p>(sem eventos)</p>");
        } else {
            sb.append("<table cellspacing='0' cellpadding='6' style='border-collapse:collapse;border:1px solid #ddd'>")
                    .append("<tr style='background:#f5f5f5'><th align='left'>Nível</th><th align='right'>Quantidade</th></tr>");
            for (Map.Entry<String, Long> e : byLevel.entrySet()) {
                sb.append("<tr>")
                        .append("<td>").append(escape(e.getKey())).append("</td>")
                        .append("<td align='right'>").append(e.getValue()).append("</td>")
                        .append("</tr>");
            }
            sb.append("<tr style='font-weight:bold'><td>Total</td><td align='right'>")
                    .append(total).append("</td></tr>")
                    .append("</table>");
        }

        // Top exceções (ERROR)
        sb.append("<h3>Top exceções (ERROR)</h3>");
        if (topExc == null || topExc.isEmpty()) {
            sb.append("<p>(sem exceções ERROR)</p>");
        } else {
            sb.append("<table cellspacing='0' cellpadding='6' style='border-collapse:collapse;border:1px solid #ddd'>")
                    .append("<tr style='background:#f5f5f5'><th align='left'>Exception</th><th align='right'>Ocorrências</th></tr>");
            for (Object[] row : topExc) {
                String ex = String.valueOf(row[0]);
                long c = ((Number) row[1]).longValue();
                sb.append("<tr><td>").append(escape(ex)).append("</td><td align='right'>").append(c).append("</td></tr>");
            }
            sb.append("</table>");
        }



        // Últimos 10 eventos
        sb.append("<h3>Últimos 10 eventos</h3>");
        if (latest == null || latest.isEmpty()) {
            sb.append("<p>(sem eventos)</p>");
        } else {
            sb.append("<table cellspacing='0' cellpadding='6' style='border-collapse:collapse;border:1px solid #ddd'>")
                    .append("<tr style='background:#f5f5f5'>")
                    .append("<th align='left'>Data/Hora (Lisbon)</th>")
                    .append("<th align='left'>Nível</th>")
                    .append("<th align='left'>Mensagem</th>")
                    .append("<th align='left'>Exceção</th>")
                    .append("</tr>");
            for (ErrorLog e : latest) {
                // Java 8: OffsetDateTime -> ZonedDateTime em Lisbon
                ZonedDateTime lisbonTs = e.getOccurredAt().toInstant().atZone(REPORT_ZONE);
                sb.append("<tr>")
                        .append("<td>").append(escape(lisbonTs.format(TS_FMT))).append("</td>")
                        .append("<td>").append(escape(nullToEmpty(e.getLevel().toString()))).append("</td>")
                        .append("<td>").append(escape(limit(nullToEmpty(e.getMessage()), 180))).append("</td>")
                        .append("<td>").append(escape(limit(nullToEmpty(e.getExceptionType()), 120))).append("</td>")
                        .append("</tr>");
            }
            sb.append("</table>");
        }

        sb.append("<p style='color:#888;font-size:12px'>Este e-mail foi gerado automaticamente pelo sistema.</p>");
        sb.append("</body></html>");
        return sb.toString();
    }

    private String escape(String s) {
        return s == null ? "" : s.replace("&","&amp;").replace("<","&lt;").replace(">","&gt;");
    }

    private String nullToEmpty(String s) { return s == null ? "" : s; }
    private String limit(String s, int n) { return s.length() > n ? s.substring(0, n) + "…" : s; }

}
