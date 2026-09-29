package com.smartlibrary.reader.service;

import com.smartlibrary.department.entity.Department;
import com.smartlibrary.department.repository.DepartmentRepository;
import com.smartlibrary.reader.dto.request.ReaderRequest;
import com.smartlibrary.reader.entity.Reader;
import com.smartlibrary.reader.repository.ReaderRepository;
import com.smartlibrary.user.entity.User;
import com.smartlibrary.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ReaderService {

    private final ReaderRepository readerRepository;
    private final UserRepository userRepository;
    private final DepartmentRepository departmentRepository;

    public Reader createReader(ReaderRequest request) {

        if (readerRepository.existsByUsn(request.getUsn())) {
            throw new RuntimeException("USN already exists");
        }

        if (readerRepository.existsByUserId(request.getUserId())) {
            throw new RuntimeException("User already has a Reader profile");
        }

        User user = userRepository.findById(request.getUserId())
                .orElseThrow(() -> new RuntimeException("User not found"));

        Department department = departmentRepository.findById(request.getDepartmentId())
                .orElseThrow(() -> new RuntimeException("Department not found"));

        Reader reader = Reader.builder()
                .user(user)
                .usn(request.getUsn())
                .department(department)
                .phone(request.getPhone())
                .build();

        return readerRepository.save(reader);
    }

    public List<Reader> getAllReaders() {
        return readerRepository.findAll();
    }

    public Reader getReaderById(Long id) {
        return readerRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Reader not found"));
    }

    public Reader getReaderByUsn(String usn) {
        return readerRepository.findByUsn(usn)
                .orElseThrow(() -> new RuntimeException("Reader not found"));
    }

    public Reader updateReader(Long id, ReaderRequest request) {

        Reader existingReader = getReaderById(id);

        if (!existingReader.getUsn().equals(request.getUsn())
                && readerRepository.existsByUsn(request.getUsn())) {
            throw new RuntimeException("USN already exists");
        }

        Department department = departmentRepository.findById(request.getDepartmentId())
                .orElseThrow(() -> new RuntimeException("Department not found"));

        existingReader.setUsn(request.getUsn());
        existingReader.setDepartment(department);
        existingReader.setPhone(request.getPhone());

        return readerRepository.save(existingReader);
    }

    public void deleteReader(Long id) {
        Reader reader = getReaderById(id);
        readerRepository.delete(reader);
    }
}