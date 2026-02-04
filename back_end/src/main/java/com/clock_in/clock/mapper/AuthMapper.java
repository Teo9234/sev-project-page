package com.clock_in.clock.mapper;

import com.clock_in.clock.dto.auth.RegisterRequestDTO;
import com.clock_in.clock.dto.auth.RegisterResponseDTO;
import com.clock_in.clock.model.Employee;
import com.clock_in.core.enums.Role;
import org.springframework.security.crypto.password.PasswordEncoder;

public class AuthMapper {

    // Convert RegisterRequestDTO → Employee entity
    public static Employee toEmployeeEntity(RegisterRequestDTO dto, PasswordEncoder passwordEncoder) {
        Employee employee = new Employee();
        employee.setFullName(dto.getFullName());
        employee.setEmail(dto.getEmail());
        employee.setPasswordHash(passwordEncoder.encode(dto.getPassword())); // hash password
        employee.setRole(Role.valueOf(dto.getRole().name())); // store enum as string
        employee.setOffice(dto.getOffice());
        employee.setOnLeave(dto.isOnLeave());
        return employee;
    }

    // Convert Employee entity → RegisterResponseDTO
    public static RegisterResponseDTO toRegisterResponseDTO(Employee employee) {
        RegisterResponseDTO dto = new RegisterResponseDTO();
        dto.setUuid(employee.getUuid().toString());
        dto.setFullName(employee.getFullName());
        dto.setEmail(employee.getEmail());
        dto.setRole(employee.getRoleEnum()); // assume Employee has a helper getter for Role enum
        dto.setOffice(employee.getOffice());
        dto.setOnLeave(employee.isOnLeave());
        return dto;
    }
}
