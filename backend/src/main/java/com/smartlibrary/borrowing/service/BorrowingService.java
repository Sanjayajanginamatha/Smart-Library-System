package com.smartlibrary.borrowing.service;

import com.smartlibrary.book.entity.Book;
import com.smartlibrary.book.repository.BookRepository;
import com.smartlibrary.borrowing.dto.request.BorrowingRequest;
import com.smartlibrary.borrowing.entity.BorrowingRecord;
import com.smartlibrary.borrowing.repository.BorrowingRecordRepository;
import com.smartlibrary.enums.BorrowingStatus;
import com.smartlibrary.reader.entity.Reader;
import com.smartlibrary.reader.repository.ReaderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class BorrowingService {

    private static final int MAX_BORROWING_LIMIT = 5;
    private static final int BORROWING_DAYS = 10;
    private static final BigDecimal FINE_PER_DAY = BigDecimal.valueOf(5);

    private final BorrowingRecordRepository borrowingRecordRepository;
    private final ReaderRepository readerRepository;
    private final BookRepository bookRepository;

    @Transactional
    public BorrowingRecord issueBook(BorrowingRequest request) {

        Reader reader = readerRepository.findById(request.getReaderId())
                .orElseThrow(() -> new RuntimeException("Reader not found"));

        Book book = bookRepository.findById(request.getBookId())
                .orElseThrow(() -> new RuntimeException("Book not found"));

        // Check whether the reader has already reached the borrowing limit
        long activeBorrowings =
                borrowingRecordRepository.countByReaderIdAndStatus(
                        reader.getId(),
                        BorrowingStatus.ISSUED
                );

        if (activeBorrowings >= MAX_BORROWING_LIMIT) {
            throw new RuntimeException(
                    "Reader cannot borrow more than 5 books"
            );
        }

        // Check whether the book has available copies
        if (book.getAvailableCopies() <= 0) {
            throw new RuntimeException(
                    "Book is currently unavailable"
            );
        }

        // Check whether this reader already has this book
        boolean alreadyBorrowed =
                borrowingRecordRepository
                        .findByReaderIdAndBookIdAndStatus(
                                reader.getId(),
                                book.getId(),
                                BorrowingStatus.ISSUED
                        )
                        .isPresent();

        if (alreadyBorrowed) {
            throw new RuntimeException(
                    "Reader has already borrowed this book"
            );
        }

        LocalDateTime issuedAt = LocalDateTime.now();

        LocalDateTime dueDate =
                issuedAt.plusDays(BORROWING_DAYS);

        BorrowingRecord borrowingRecord = BorrowingRecord.builder()
                .reader(reader)
                .book(book)
                .issuedAt(issuedAt)
                .dueDate(dueDate)
                .returnedAt(null)
                .status(BorrowingStatus.ISSUED)
                .fineAmount(BigDecimal.ZERO)
                .build();

        // Decrease available copies
        book.setAvailableCopies(
                book.getAvailableCopies() - 1
        );

        bookRepository.save(book);

        return borrowingRecordRepository.save(borrowingRecord);
    }

    public List<BorrowingRecord> getAllBorrowings() {
        return borrowingRecordRepository.findAll();
    }

    public BorrowingRecord getBorrowingById(Long id) {

        return borrowingRecordRepository.findById(id)
                .orElseThrow(
                        () -> new RuntimeException("Borrowing record not found")
                );
    }

    public List<BorrowingRecord> getBorrowingsByReader(Long readerId) {

        if (!readerRepository.existsById(readerId)) {
            throw new RuntimeException("Reader not found");
        }

        return borrowingRecordRepository.findByReaderId(readerId);
    }

    public List<BorrowingRecord> getActiveBorrowings() {

        return borrowingRecordRepository.findByStatus(
                BorrowingStatus.ISSUED
        );
    }

    @Transactional
    public BorrowingRecord returnBook(Long id) {

        BorrowingRecord borrowingRecord = getBorrowingById(id);

        if (borrowingRecord.getStatus() == BorrowingStatus.RETURNED) {
            throw new RuntimeException(
                    "Book has already been returned"
            );
        }

        LocalDateTime returnedAt = LocalDateTime.now();

        BigDecimal fineAmount = calculateFine(
                borrowingRecord.getDueDate(),
                returnedAt
        );

        borrowingRecord.setReturnedAt(returnedAt);
        borrowingRecord.setStatus(BorrowingStatus.RETURNED);
        borrowingRecord.setFineAmount(fineAmount);

        Book book = borrowingRecord.getBook();

        book.setAvailableCopies(
                book.getAvailableCopies() + 1
        );

        bookRepository.save(book);

        return borrowingRecordRepository.save(borrowingRecord);
    }

    private BigDecimal calculateFine(
            LocalDateTime dueDate,
            LocalDateTime returnedAt) {

        if (!returnedAt.isAfter(dueDate)) {
            return BigDecimal.ZERO;
        }

        long overdueDays =
                java.time.temporal.ChronoUnit.DAYS.between(
                        dueDate.toLocalDate(),
                        returnedAt.toLocalDate()
                );

        return FINE_PER_DAY.multiply(
                BigDecimal.valueOf(overdueDays)
        );
    }
}