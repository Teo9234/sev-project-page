package com.clock_in.clock.dto;

import com.clock_in.core.enums.Role;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class EmployeeRequestDTO {

    @NotBlank(message = "Full name is required")
    private String fullName;

    @Email(message = "Must be a valid email")
    @NotBlank(message = "Email is required")
    private String email;

    @NotBlank(message = "Password is required")
    private String password;

    @NotBlank(message = "Office is required")
    private String office;

    @NotNull(message = "Role is required")
    private Role role;

    private boolean isOnLeave;
}
