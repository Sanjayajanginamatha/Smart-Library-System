package com.smartlibrary.book.dto.response;

import com.smartlibrary.enums.BookStatus;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BookResponse {

    private Long id;
    private String isbn;
    private String title;
    private String author;
    private String publisher;
    private String category;
    private Integer totalCopies;
    private Integer availableCopies;
    private BookStatus status;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}