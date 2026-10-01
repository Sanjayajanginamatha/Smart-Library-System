package com.smartlibrary.book.service;

import com.smartlibrary.book.dto.request.BookRequest;
import com.smartlibrary.book.entity.Book;
import com.smartlibrary.book.repository.BookRepository;
import com.smartlibrary.enums.BookStatus;
import com.smartlibrary.exception.BusinessException;
import com.smartlibrary.exception.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BookService {

    private final BookRepository bookRepository;

    public Book createBook(BookRequest request) {

        if (bookRepository.existsByIsbn(request.getIsbn())) {
            throw new BusinessException("ISBN already exists");
        }

        Book book = Book.builder()
                .isbn(request.getIsbn())
                .title(request.getTitle())
                .author(request.getAuthor())
                .publisher(request.getPublisher())
                .category(request.getCategory())
                .totalCopies(request.getTotalCopies())
                .availableCopies(request.getTotalCopies())
                .status(BookStatus.ACTIVE)
                .build();

        return bookRepository.save(book);
    }

    public List<Book> getAllBooks() {
        return bookRepository.findAll();
    }

    public Book getBookById(Long id) {

        return bookRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Book not found"));
    }

    public Book getBookByIsbn(String isbn) {

        return bookRepository.findByIsbn(isbn)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Book not found"));
    }

    public Book updateBook(Long id, BookRequest request) {

        Book existingBook = getBookById(id);

        if (!existingBook.getIsbn().equals(request.getIsbn())
                && bookRepository.existsByIsbn(request.getIsbn())) {

            throw new BusinessException("ISBN already exists");
        }

        int issuedCopies =
                existingBook.getTotalCopies()
                        - existingBook.getAvailableCopies();

        int newTotalCopies = request.getTotalCopies();

        if (newTotalCopies < issuedCopies) {

            throw new BusinessException(
                    "Total copies cannot be less than currently issued copies"
            );
        }

        existingBook.setIsbn(request.getIsbn());
        existingBook.setTitle(request.getTitle());
        existingBook.setAuthor(request.getAuthor());
        existingBook.setPublisher(request.getPublisher());
        existingBook.setCategory(request.getCategory());
        existingBook.setTotalCopies(newTotalCopies);
        existingBook.setAvailableCopies(newTotalCopies - issuedCopies);

        return bookRepository.save(existingBook);
    }

    public void deleteBook(Long id) {

        Book book = getBookById(id);

        bookRepository.delete(book);
    }
}