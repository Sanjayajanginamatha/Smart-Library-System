package com.smartlibrary.borrowing.controller;

import com.smartlibrary.borrowing.dto.request.BorrowingRequest;
import com.smartlibrary.borrowing.dto.response.BorrowingResponse;
import com.smartlibrary.borrowing.entity.BorrowingRecord;
import com.smartlibrary.borrowing.mapper.BorrowingMapper;
import com.smartlibrary.borrowing.service.BorrowingService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/borrowings")
@RequiredArgsConstructor
public class BorrowingController {

    private final BorrowingService borrowingService;
    private final BorrowingMapper borrowingMapper;

    @PostMapping
    @PreAuthorize("hasRole('RECEPTIONIST')")
    public ResponseEntity<BorrowingResponse> issueBook(
            @Valid @RequestBody BorrowingRequest request) {

        BorrowingRecord borrowingRecord =
                borrowingService.issueBook(request);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(borrowingMapper.toResponse(borrowingRecord));
    }

    @GetMapping
    public ResponseEntity<List<BorrowingResponse>> getAllBorrowings() {

        List<BorrowingResponse> responses =
                borrowingService.getAllBorrowings()
                        .stream()
                        .map(borrowingMapper::toResponse)
                        .toList();

        return ResponseEntity.ok(responses);
    }

    @GetMapping("/{id}")
    public ResponseEntity<BorrowingResponse> getBorrowingById(
            @PathVariable Long id) {

        BorrowingRecord borrowingRecord =
                borrowingService.getBorrowingById(id);

        return ResponseEntity.ok(
                borrowingMapper.toResponse(borrowingRecord)
        );
    }

    @GetMapping("/reader/{readerId}")
    public ResponseEntity<List<BorrowingResponse>> getBorrowingsByReader(
            @PathVariable Long readerId) {

        List<BorrowingResponse> responses =
                borrowingService.getBorrowingsByReader(readerId)
                        .stream()
                        .map(borrowingMapper::toResponse)
                        .toList();

        return ResponseEntity.ok(responses);
    }

    @GetMapping("/active")
    public ResponseEntity<List<BorrowingResponse>> getActiveBorrowings() {

        List<BorrowingResponse> responses =
                borrowingService.getActiveBorrowings()
                        .stream()
                        .map(borrowingMapper::toResponse)
                        .toList();

        return ResponseEntity.ok(responses);
    }

    @PutMapping("/{id}/return")
    @PreAuthorize("hasRole('RECEPTIONIST')")
    public ResponseEntity<BorrowingResponse> returnBook(
            @PathVariable Long id) {

        BorrowingRecord borrowingRecord =
                borrowingService.returnBook(id);

        return ResponseEntity.ok(
                borrowingMapper.toResponse(borrowingRecord)
        );
    }
}