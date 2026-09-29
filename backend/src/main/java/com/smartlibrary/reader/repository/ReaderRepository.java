package com.smartlibrary.reader.repository;

import com.smartlibrary.reader.entity.Reader;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ReaderRepository extends JpaRepository<Reader, Long> {

    Optional<Reader> findByUsn(String usn);

    boolean existsByUsn(String usn);

    Optional<Reader> findByUserId(Long userId);

    boolean existsByUserId(Long userId);
}