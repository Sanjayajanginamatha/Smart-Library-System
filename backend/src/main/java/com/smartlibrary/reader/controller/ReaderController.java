package com.smartlibrary.reader.controller;

import com.smartlibrary.reader.dto.request.ReaderRequest;
import com.smartlibrary.reader.dto.response.ReaderResponse;
import com.smartlibrary.reader.entity.Reader;
import com.smartlibrary.reader.mapper.ReaderMapper;
import com.smartlibrary.reader.service.ReaderService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/readers")
@RequiredArgsConstructor
public class ReaderController {

    private final ReaderService readerService;
    private final ReaderMapper readerMapper;

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ReaderResponse> createReader(
            @Valid @RequestBody ReaderRequest request) {

        Reader reader = readerService.createReader(request);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(readerMapper.toResponse(reader));
    }

    @GetMapping
    public ResponseEntity<List<ReaderResponse>> getAllReaders() {

        List<ReaderResponse> responses = readerService.getAllReaders()
                .stream()
                .map(readerMapper::toResponse)
                .toList();

        return ResponseEntity.ok(responses);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ReaderResponse> getReaderById(
            @PathVariable Long id) {

        Reader reader = readerService.getReaderById(id);

        return ResponseEntity.ok(
                readerMapper.toResponse(reader)
        );
    }

    @GetMapping("/usn/{usn}")
    public ResponseEntity<ReaderResponse> getReaderByUsn(
            @PathVariable String usn) {

        Reader reader = readerService.getReaderByUsn(usn);

        return ResponseEntity.ok(
                readerMapper.toResponse(reader)
        );
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ReaderResponse> updateReader(
            @PathVariable Long id,
            @Valid @RequestBody ReaderRequest request) {

        Reader reader = readerService.updateReader(id, request);

        return ResponseEntity.ok(
                readerMapper.toResponse(reader)
        );
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteReader(
            @PathVariable Long id) {

        readerService.deleteReader(id);

        return ResponseEntity.noContent().build();
    }
}