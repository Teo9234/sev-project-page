package com.clock_in.clock.dto.auth;

import com.clock_in.core.enums.Role;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RegisterResponseDTO {

    private String uuid;
    private String fullName;
    private String email;
    private Role role;
    private String office;
    private boolean isOnLeave;
}
