package com.clock_in.clock.dto.auth;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

/**
 * DTO for login request, contains the email and password for authentication.
 */
@Getter
@Setter
public class LoginRequestDTO {

    @Email(message = "Invalid email format")
    @NotBlank(message = "Email cannot be empty")
    private String email; // User's email for login

    @NotBlank(message = "Password cannot be empty")
    private String password; // User's password for login
}
