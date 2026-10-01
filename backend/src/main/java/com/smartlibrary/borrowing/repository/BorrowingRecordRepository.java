package com.smartlibrary.borrowing.repository;

import com.smartlibrary.borrowing.entity.BorrowingRecord;
import com.smartlibrary.enums.BorrowingStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface BorrowingRecordRepository
        extends JpaRepository<BorrowingRecord, Long> {

    List<BorrowingRecord> findByReaderId(Long readerId);

    List<BorrowingRecord> findByBookId(Long bookId);

    List<BorrowingRecord> findByStatus(BorrowingStatus status);

    List<BorrowingRecord> findByReaderIdAndStatus(
            Long readerId,
            BorrowingStatus status
    );

    Optional<BorrowingRecord> findByReaderIdAndBookIdAndStatus(
            Long readerId,
            Long bookId,
            BorrowingStatus status
    );

    long countByReaderIdAndStatus(
            Long readerId,
            BorrowingStatus status
    );
}