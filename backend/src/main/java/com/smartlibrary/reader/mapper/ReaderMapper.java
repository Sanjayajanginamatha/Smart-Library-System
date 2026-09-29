package com.smartlibrary.reader.mapper;

import com.smartlibrary.department.entity.Department;
import com.smartlibrary.reader.dto.response.ReaderResponse;
import com.smartlibrary.reader.entity.Reader;
import com.smartlibrary.user.entity.User;
import org.springframework.stereotype.Component;

@Component
public class ReaderMapper {

    public ReaderResponse toResponse(Reader reader) {

        User user = reader.getUser();
        Department department = reader.getDepartment();

        return ReaderResponse.builder()
                .id(reader.getId())
                .userId(user.getId())
                .fullName(user.getFullName())
                .email(user.getEmail())
                .usn(reader.getUsn())
                .departmentId(department.getId())
                .departmentName(department.getName())
                .departmentCode(department.getCode())
                .phone(reader.getPhone())
                .build();
    }
}