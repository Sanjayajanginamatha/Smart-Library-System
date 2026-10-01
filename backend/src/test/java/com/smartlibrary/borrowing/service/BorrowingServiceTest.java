package com.smartlibrary.borrowing.service;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

import static org.junit.jupiter.api.Assertions.assertEquals;

class BorrowingServiceTest {

    private static final BigDecimal FINE_PER_DAY = BigDecimal.valueOf(5);

    @Test
    void shouldCalculateFineForLateReturn() {

        LocalDateTime dueDate =
                LocalDateTime.now().minusDays(10);

        LocalDateTime returnedAt =
                LocalDateTime.now();

        long overdueDays =
                ChronoUnit.DAYS.between(
                        dueDate.toLocalDate(),
                        returnedAt.toLocalDate()
                );

        BigDecimal fine =
                FINE_PER_DAY.multiply(
                        BigDecimal.valueOf(overdueDays)
                );

        assertEquals(
                BigDecimal.valueOf(50),
                fine
        );
    }
}