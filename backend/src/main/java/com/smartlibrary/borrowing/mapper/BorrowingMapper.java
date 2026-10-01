package com.smartlibrary.borrowing.mapper;

import com.smartlibrary.borrowing.dto.response.BorrowingResponse;
import com.smartlibrary.borrowing.entity.BorrowingRecord;
import com.smartlibrary.book.entity.Book;
import com.smartlibrary.reader.entity.Reader;
import com.smartlibrary.user.entity.User;
import org.springframework.stereotype.Component;

@Component
public class BorrowingMapper {

    public BorrowingResponse toResponse(BorrowingRecord borrowingRecord) {

        Reader reader = borrowingRecord.getReader();
        Book book = borrowingRecord.getBook();
        User user = reader.getUser();

        return BorrowingResponse.builder()
                .id(borrowingRecord.getId())

                .readerId(reader.getId())
                .readerName(user.getFullName())
                .usn(reader.getUsn())

                .bookId(book.getId())
                .bookTitle(book.getTitle())
                .isbn(book.getIsbn())

                .issuedAt(borrowingRecord.getIssuedAt())
                .dueDate(borrowingRecord.getDueDate())
                .returnedAt(borrowingRecord.getReturnedAt())

                .status(borrowingRecord.getStatus())
                .fineAmount(borrowingRecord.getFineAmount())

                .createdAt(borrowingRecord.getCreatedAt())
                .updatedAt(borrowingRecord.getUpdatedAt())

                .build();
    }
}