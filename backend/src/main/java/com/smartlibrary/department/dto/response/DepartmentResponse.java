package com.smartlibrary.department.dto.response;

import com.smartlibrary.enums.DepartmentStatus;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DepartmentResponse {

    private Long id;

    private String name;

    private String code;

    private String description;

    private DepartmentStatus status;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;
}