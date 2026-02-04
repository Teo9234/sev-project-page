package com.clock_in.clock.dto.auth;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LoginResponseDTO {

    private String token;          // JWT token
    private String fullName;
    private String email;
    private String role;
}
