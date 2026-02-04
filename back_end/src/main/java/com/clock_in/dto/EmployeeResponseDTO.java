package com.clock_in.dto;

import com.clock_in.core.enums.Role;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class EmployeeResponseDTO {

    private String uuid;       // safe public identifier
    private String fullName;
    private String email;
    private Role role;
    private String office;
    private boolean isOnLeave;
    private boolean isCurrentlyWorking; // computed from clock entries
}
