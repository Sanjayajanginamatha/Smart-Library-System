package com.smartlibrary.reader.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ReaderRequest {

    @NotNull(message = "User ID is required")
    private Long userId;

    @NotBlank(message = "USN is required")
    private String usn;

    @NotNull(message = "Department ID is required")
    private Long departmentId;

    @NotBlank(message = "Phone number is required")
    private String phone;
}