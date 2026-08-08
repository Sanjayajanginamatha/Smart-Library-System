package com.smartlibrary.auth.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class VerifyOtpRequest {

    @NotBlank
    private String email;

    @NotBlank
    private String otp;

}