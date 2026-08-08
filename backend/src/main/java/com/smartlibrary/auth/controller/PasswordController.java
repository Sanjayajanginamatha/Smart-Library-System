package com.smartlibrary.auth.controller;

import com.smartlibrary.auth.dto.request.ChangePasswordRequest;
import com.smartlibrary.auth.dto.request.ForgotPasswordRequest;
import com.smartlibrary.auth.dto.request.ResetPasswordRequest;
import com.smartlibrary.auth.dto.request.VerifyOtpRequest;
import com.smartlibrary.auth.dto.response.PasswordResponse;
import com.smartlibrary.auth.service.PasswordService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/password")
@RequiredArgsConstructor
public class PasswordController {

    private final PasswordService passwordService;

    @PostMapping("/forgot-password")
    public PasswordResponse forgotPassword(
            @Valid @RequestBody ForgotPasswordRequest request) {

        return passwordService.forgotPassword(request);
    }

    @PostMapping("/verify-otp")
    public PasswordResponse verifyOtp(
            @Valid @RequestBody VerifyOtpRequest request) {

        return passwordService.verifyOtp(request);
    }

    @PostMapping("/reset-password")
    public PasswordResponse resetPassword(
            @Valid @RequestBody ResetPasswordRequest request) {

        return passwordService.resetPassword(request);
    }

    @PostMapping("/change-password")
    public PasswordResponse changePassword(
            @Valid @RequestBody ChangePasswordRequest request) {

        return passwordService.changePassword(request);
    }
}