package com.smartlibrary.book.mapper;

import com.smartlibrary.book.dto.response.BookResponse;
import com.smartlibrary.book.entity.Book;
import org.springframework.stereotype.Component;

@Component
public class BookMapper {

    public BookResponse toResponse(Book book) {

        return BookResponse.builder()
                .id(book.getId())
                .isbn(book.getIsbn())
                .title(book.getTitle())
                .author(book.getAuthor())
                .publisher(book.getPublisher())
                .category(book.getCategory())
                .totalCopies(book.getTotalCopies())
                .availableCopies(book.getAvailableCopies())
                .status(book.getStatus())
                .createdAt(book.getCreatedAt())
                .updatedAt(book.getUpdatedAt())
                .build();
    }
}