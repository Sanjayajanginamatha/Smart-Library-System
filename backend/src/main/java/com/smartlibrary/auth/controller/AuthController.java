package com.smartlibrary.auth.controller;

import com.smartlibrary.auth.dto.request.LoginRequest;
import com.smartlibrary.auth.dto.request.RegisterRequest;
import com.smartlibrary.common.response.ApiResponse;
import com.smartlibrary.auth.dto.response.LoginResponse;
import com.smartlibrary.auth.service.AuthenticationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthenticationService authenticationService;

    @PostMapping("/register")
    public ApiResponse register(
            @Valid @RequestBody RegisterRequest request) {

        return authenticationService.register(request);
    }

    @PostMapping("/login")
    public LoginResponse login(
            @Valid @RequestBody LoginRequest request) {

        return authenticationService.login(request);
    }

}