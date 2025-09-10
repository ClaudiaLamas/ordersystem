package com.claudialamas.errorLog;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.OffsetDateTime;
import java.util.List;

public interface ErrorLogRepository extends JpaRepository<ErrorLog, Long> {

    Page<ErrorLog> findByOccurredAtBetween(OffsetDateTime start, OffsetDateTime end, Pageable pageable);

    @Query("select e.level, count(e) " +
            "from ErrorLog e " +
            "where e.occurredAt >= :start and e.occurredAt < :end " +
            "group by e.level " +
            "order by count(e) desc")
    List<Object[]> countByLevelBetween(@Param("start") OffsetDateTime start,
                                       @Param("end") OffsetDateTime end);

    @Query("select e.exceptionType, count(e) " +
            "from ErrorLog e " +
            "where e.occurredAt >= :start and e.occurredAt < :end " +
            "  and e.level = :level " +
            "  and e.exceptionType is not null " +
            "group by e.exceptionType order by count(e) desc")
    List<Object[]> topExceptionTypesBetween(@Param("start") OffsetDateTime start,
                                            @Param("end")   OffsetDateTime end,
                                            @Param("level") ErrorLevel level);

    // Últimos N eventos do dia (usar Pageable para limitar)
    List<ErrorLog> findByOccurredAtBetweenOrderByOccurredAtDesc(OffsetDateTime start,
                                                                OffsetDateTime end,
                                                                Pageable pageable);
}
