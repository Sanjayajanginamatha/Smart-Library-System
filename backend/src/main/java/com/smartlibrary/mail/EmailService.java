package com.smartlibrary.mail;

public interface EmailService {

    void sendTemporaryPassword(
            String toEmail,
            String fullName,
            String temporaryPassword
    );

    void sendOtp(
            String toEmail,
            String otp
    );
}