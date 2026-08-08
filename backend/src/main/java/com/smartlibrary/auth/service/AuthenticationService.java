package com.smartlibrary.auth.service;

import com.smartlibrary.auth.dto.request.LoginRequest;
import com.smartlibrary.auth.dto.request.RegisterRequest;
import com.smartlibrary.auth.dto.response.LoginResponse;
import com.smartlibrary.auth.jwt.JwtService;
import com.smartlibrary.common.response.ApiResponse;
import com.smartlibrary.common.util.PasswordGenerator;
import com.smartlibrary.enums.UserStatus;
import com.smartlibrary.mail.EmailService;
import com.smartlibrary.user.entity.User;
import com.smartlibrary.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthenticationService {

    private final UserService userService;
    private final PasswordEncoder passwordEncoder;
    private final EmailService emailService;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    public ApiResponse register(RegisterRequest request) {

        if (userService.existsByEmail(request.getEmail())) {
            return ApiResponse.builder()
                    .success(false)
                    .message("Email already exists")
                    .build();
        }

        String temporaryPassword = PasswordGenerator.generatePassword(10);

        User user = User.builder()
                .fullName(request.getFullName())
                .email(request.getEmail())
                .password(passwordEncoder.encode(temporaryPassword))
                .role(request.getRole())
                .status(UserStatus.ACTIVE)
                .firstLogin(true)
                .build();

        userService.save(user);

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

    public LoginResponse login(LoginRequest request) {

        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getEmail(),
                        request.getPassword()
                )
        );

        User user = userService.findByEmail(request.getEmail())
                .orElseThrow(() -> new RuntimeException("User not found"));

        String token = jwtService.generateToken(user.getEmail());

        return LoginResponse.builder()
                .success(true)
                .message("Login successful")
                .token(token)
                .firstLogin(user.getFirstLogin())
                .role(user.getRole().name())
                .build();
    }
}