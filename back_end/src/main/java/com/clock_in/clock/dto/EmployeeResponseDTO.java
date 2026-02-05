package com.clock_in.clock.dto;

import com.clock_in.clock.model.Employee;
import com.clock_in.core.enums.Role;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class EmployeeResponseDTO {

    private String uuid;
    private String fullName;
    private String email;
    private String office;
    private Role role;
    private boolean isOnLeave;
    private boolean currentlyWorking; // optional computed field

    public static EmployeeResponseDTO fromEntity(Employee employee) {
        EmployeeResponseDTO dto = new EmployeeResponseDTO();
        dto.setUuid(employee.getUuid().toString());
        dto.setFullName(employee.getFullName());
        dto.setEmail(employee.getEmail());
        dto.setOffice(employee.getOffice());
        dto.setRole(employee.getRole());
        dto.setOnLeave(employee.isOnLeave());
        dto.setCurrentlyWorking(employee.isCurrentlyWorking());
        return dto;
    }
}
