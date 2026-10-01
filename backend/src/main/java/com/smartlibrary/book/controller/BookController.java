package com.smartlibrary.book.controller;

import com.smartlibrary.book.dto.request.BookRequest;
import com.smartlibrary.book.dto.response.BookResponse;
import com.smartlibrary.book.entity.Book;
import com.smartlibrary.book.mapper.BookMapper;
import com.smartlibrary.book.service.BookService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/books")
@RequiredArgsConstructor
public class BookController {

    private final BookService bookService;
    private final BookMapper bookMapper;

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<BookResponse> createBook(
            @Valid @RequestBody BookRequest request) {

        Book book = bookService.createBook(request);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(bookMapper.toResponse(book));
    }

    @GetMapping
    public ResponseEntity<List<BookResponse>> getAllBooks() {

        List<BookResponse> responses = bookService.getAllBooks()
                .stream()
                .map(bookMapper::toResponse)
                .toList();

        return ResponseEntity.ok(responses);
    }

    @GetMapping("/{id}")
    public ResponseEntity<BookResponse> getBookById(
            @PathVariable Long id) {

        Book book = bookService.getBookById(id);

        return ResponseEntity.ok(
                bookMapper.toResponse(book)
        );
    }

    @GetMapping("/isbn/{isbn}")
    public ResponseEntity<BookResponse> getBookByIsbn(
            @PathVariable String isbn) {

        Book book = bookService.getBookByIsbn(isbn);

        return ResponseEntity.ok(
                bookMapper.toResponse(book)
        );
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<BookResponse> updateBook(
            @PathVariable Long id,
            @Valid @RequestBody BookRequest request) {

        Book book = bookService.updateBook(id, request);

        return ResponseEntity.ok(
                bookMapper.toResponse(book)
        );
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteBook(
            @PathVariable Long id) {

        bookService.deleteBook(id);

        return ResponseEntity.noContent().build();
    }
}