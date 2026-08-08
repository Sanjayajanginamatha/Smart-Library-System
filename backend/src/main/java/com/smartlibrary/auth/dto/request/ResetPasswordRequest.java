package com.smartlibrary.auth.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ResetPasswordRequest {

    @Email
    @NotBlank
    private String email;

    @NotBlank
    private String otp;

    @NotBlank
    private String newPassword;

    @NotBlank
    private String confirmPassword;

}