package com.smartlibrary.controller;

import com.smartlibrary.dto.request.RegisterRequest;
import com.smartlibrary.dto.response.ApiResponse;
import com.smartlibrary.service.AuthenticationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthenticationService authenticationService;

    @PostMapping("/register")
    public ApiResponse register(@Valid @RequestBody RegisterRequest request) {

        return authenticationService.register(request);

    }

}