package com.smartlibrary.service;

import com.smartlibrary.mail.EmailService;
import com.smartlibrary.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import com.smartlibrary.dto.request.RegisterRequest;
import com.smartlibrary.dto.response.ApiResponse;
import com.smartlibrary.entity.User;
import com.smartlibrary.enums.UserStatus;
import com.smartlibrary.util.PasswordGenerator;

@Service
@RequiredArgsConstructor
public class AuthenticationService {

    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    private final EmailService emailService;
    public ApiResponse register(RegisterRequest request) {

        if (userRepository.existsByEmail(request.getEmail())) {

            return ApiResponse.builder()
                    .success(false)
                    .message("Email already exists")
                    .build();

        }

        String temporaryPassword = PasswordGenerator.generatePassword(10);

        String encodedPassword =
                passwordEncoder.encode(temporaryPassword);

        User user = User.builder()
                .fullName(request.getFullName())
                .email(request.getEmail())
                .password(encodedPassword)
                .role(request.getRole())
                .status(UserStatus.ACTIVE)
                .firstLogin(true)
                .build();

        userRepository.save(user);

        emailService.sendTemporaryPassword(
                user.getEmail(),
                user.getFullName(),
                temporaryPassword
        );

        return ApiResponse.builder()
                .success(true)
                .message("User registered successfully")
                .build();

    }

}