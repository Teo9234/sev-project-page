package com.clock_in.clock.dto.auth;

import lombok.Getter;
import lombok.Setter;

/**
 * DTO for login response, contains the generated JWT token and user details.
 */
@Getter
@Setter
public class LoginResponseDTO {

    private String token;  // The JWT token issued on successful login
    private String uuid;  // The UUID of the logged-in employee
    private String fullName;  // The full name of the logged-in employee
    private String email;  // The email of the logged-in employee
    private String role;  // The role of the logged-in employee
    private String office;  // The office of the logged-in employee
    private boolean isOnLeave;  // Whether the employee is currently on leave

}
