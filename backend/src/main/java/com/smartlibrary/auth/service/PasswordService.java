package com.smartlibrary.auth.service;

import com.smartlibrary.auth.dto.request.ChangePasswordRequest;
import com.smartlibrary.auth.dto.request.ForgotPasswordRequest;
import com.smartlibrary.auth.dto.request.ResetPasswordRequest;
import com.smartlibrary.auth.dto.request.VerifyOtpRequest;
import com.smartlibrary.auth.dto.response.PasswordResponse;
import com.smartlibrary.auth.entity.PasswordResetOtp;
import com.smartlibrary.auth.repository.PasswordResetOtpRepository;
import com.smartlibrary.mail.EmailService;
import com.smartlibrary.user.entity.User;
import com.smartlibrary.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Random;

@Service
@RequiredArgsConstructor
public class PasswordService {

    private static final int OTP_EXPIRATION_MINUTES = 10;

    private final UserService userService;
    private final PasswordResetOtpRepository otpRepository;
    private final PasswordEncoder passwordEncoder;
    private final EmailService emailService;

    public PasswordResponse forgotPassword(ForgotPasswordRequest request) {

        User user = userService.findByEmail(request.getEmail())
                .orElse(null);

        /*
         * We return the same message even when the email
         * does not exist. This prevents user enumeration.
         */
        if (user == null) {
            return PasswordResponse.builder()
                    .success(true)
                    .message("If the email exists, an OTP has been sent.")
                    .build();
        }

        String otp = generateOtp();

        PasswordResetOtp passwordResetOtp = PasswordResetOtp.builder()
                .email(user.getEmail())
                .otp(otp)
                .expiryTime(
                        LocalDateTime.now()
                                .plusMinutes(OTP_EXPIRATION_MINUTES)
                )
                .verified(false)
                .used(false)
                .build();

        otpRepository.save(passwordResetOtp);

        emailService.sendOtp(
                user.getEmail(),
                otp
        );

        return PasswordResponse.builder()
                .success(true)
                .message("If the email exists, an OTP has been sent.")
                .build();
    }

    public PasswordResponse verifyOtp(VerifyOtpRequest request) {

        PasswordResetOtp resetOtp =
                otpRepository.findTopByEmailOrderByCreatedAtDesc(
                        request.getEmail()
                ).orElse(null);

        if (resetOtp == null) {
            return PasswordResponse.builder()
                    .success(false)
                    .message("Invalid OTP")
                    .build();
        }

        if (Boolean.TRUE.equals(resetOtp.getUsed())) {
            return PasswordResponse.builder()
                    .success(false)
                    .message("OTP has already been used")
                    .build();
        }

        if (resetOtp.getExpiryTime().isBefore(LocalDateTime.now())) {
            return PasswordResponse.builder()
                    .success(false)
                    .message("OTP has expired")
                    .build();
        }

        if (!resetOtp.getOtp().equals(request.getOtp())) {
            return PasswordResponse.builder()
                    .success(false)
                    .message("Invalid OTP")
                    .build();
        }

        resetOtp.setVerified(true);
        otpRepository.save(resetOtp);

        return PasswordResponse.builder()
                .success(true)
                .message("OTP verified successfully")
                .build();
    }

    public PasswordResponse resetPassword(
            ResetPasswordRequest request) {

        if (!request.getNewPassword()
                .equals(request.getConfirmPassword())) {

            return PasswordResponse.builder()
                    .success(false)
                    .message("Passwords do not match")
                    .build();
        }

        PasswordResetOtp resetOtp =
                otpRepository.findTopByEmailOrderByCreatedAtDesc(
                        request.getEmail()
                ).orElse(null);

        if (resetOtp == null) {
            return PasswordResponse.builder()
                    .success(false)
                    .message("Invalid password reset request")
                    .build();
        }

        if (!Boolean.TRUE.equals(resetOtp.getVerified())) {
            return PasswordResponse.builder()
                    .success(false)
                    .message("OTP verification required")
                    .build();
        }

        if (Boolean.TRUE.equals(resetOtp.getUsed())) {
            return PasswordResponse.builder()
                    .success(false)
                    .message("Password reset request already used")
                    .build();
        }

        if (resetOtp.getExpiryTime().isBefore(LocalDateTime.now())) {
            return PasswordResponse.builder()
                    .success(false)
                    .message("OTP has expired")
                    .build();
        }

        User user = userService.findByEmail(request.getEmail())
                .orElse(null);

        if (user == null) {
            return PasswordResponse.builder()
                    .success(false)
                    .message("Unable to reset password")
                    .build();
        }

        user.setPassword(
                passwordEncoder.encode(request.getNewPassword())
        );

        user.setFirstLogin(false);

        userService.save(user);

        resetOtp.setUsed(true);
        otpRepository.save(resetOtp);

        return PasswordResponse.builder()
                .success(true)
                .message("Password reset successfully")
                .build();
    }

    public PasswordResponse changePassword(
            ChangePasswordRequest request) {

        User user = userService.findByEmail(request.getEmail())
                .orElse(null);

        if (user == null) {
            return PasswordResponse.builder()
                    .success(false)
                    .message("Invalid email or password")
                    .build();
        }

        if (!passwordEncoder.matches(
                request.getCurrentPassword(),
                user.getPassword())) {

            return PasswordResponse.builder()
                    .success(false)
                    .message("Invalid email or password")
                    .build();
        }

        if (request.getCurrentPassword()
                .equals(request.getNewPassword())) {

            return PasswordResponse.builder()
                    .success(false)
                    .message("New password must be different from current password")
                    .build();
        }

        user.setPassword(
                passwordEncoder.encode(request.getNewPassword())
        );

        user.setFirstLogin(false);

        userService.save(user);

        return PasswordResponse.builder()
                .success(true)
                .message("Password changed successfully")
                .build();
    }

    private String generateOtp() {

        return String.format(
                "%06d",
                new Random().nextInt(1_000_000)
        );
    }
}