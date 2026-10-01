package com.smartlibrary.borrowing.dto.response;

import com.smartlibrary.enums.BorrowingStatus;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BorrowingResponse {

    private Long id;

    private Long readerId;
    private String readerName;
    private String usn;

    private Long bookId;
    private String bookTitle;
    private String isbn;

    private LocalDateTime issuedAt;
    private LocalDateTime dueDate;
    private LocalDateTime returnedAt;

    private BorrowingStatus status;

    private BigDecimal fineAmount;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}