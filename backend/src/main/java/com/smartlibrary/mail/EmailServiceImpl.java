package com.smartlibrary.mail;

import lombok.RequiredArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EmailServiceImpl implements EmailService {

    private final JavaMailSender mailSender;

    @Override
    public void sendTemporaryPassword(
            String toEmail,
            String fullName,
            String temporaryPassword) {

        SimpleMailMessage message = new SimpleMailMessage();

        message.setTo(toEmail);
        message.setSubject("Welcome to Smart Library");

        message.setText(
                "Hello " + fullName + ",\n\n" +
                        "Your Smart Library account has been created successfully.\n\n" +
                        "Temporary Password : " + temporaryPassword + "\n\n" +
                        "Please login and change your password immediately.\n\n" +
                        "Thank You,\n" +
                        "Smart Library Team"
        );

        mailSender.send(message);
    }

    @Override
    public void sendOtp(String toEmail, String otp) {

        SimpleMailMessage message = new SimpleMailMessage();

        message.setTo(toEmail);
        message.setSubject("Smart Library Password Reset OTP");

        message.setText(
                "Your OTP for password reset is : " +
                        otp +
                        "\n\nThis OTP is valid for 10 minutes.\n\n" +
                        "If you did not request this, please ignore this email."
        );

        mailSender.send(message);
    }
}