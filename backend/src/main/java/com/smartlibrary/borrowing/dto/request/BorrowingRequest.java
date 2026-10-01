package com.smartlibrary.borrowing.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BorrowingRequest {

    @NotNull(message = "Reader ID is required")
    private Long readerId;

    @NotNull(message = "Book ID is required")
    private Long bookId;
}