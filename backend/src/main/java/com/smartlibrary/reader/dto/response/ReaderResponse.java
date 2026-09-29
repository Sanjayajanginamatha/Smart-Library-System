package com.smartlibrary.reader.dto.response;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ReaderResponse {

    private Long id;

    private Long userId;

    private String fullName;

    private String email;

    private String usn;

    private Long departmentId;

    private String departmentName;

    private String departmentCode;

    private String phone;
}