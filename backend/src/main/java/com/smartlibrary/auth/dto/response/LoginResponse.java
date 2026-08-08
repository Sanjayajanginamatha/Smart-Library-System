package com.smartlibrary.auth.dto.response;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LoginResponse {

    private boolean success;

    private String message;

    private String token;

    private boolean firstLogin;

    private String role;

}