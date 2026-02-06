package com.clock_in.clock.mapper;

import com.clock_in.clock.dto.auth.RegisterRequestDTO;
import com.clock_in.clock.dto.auth.RegisterResponseDTO;
import com.clock_in.clock.model.Employee;
import com.clock_in.clock.service.EmployeeService;
import com.clock_in.core.enums.Role;
import org.springframework.security.crypto.password.PasswordEncoder;

public class AuthMapper {

    // Convert RegisterRequestDTO → Employee entity
    public static Employee toEmployeeEntity(RegisterRequestDTO dto) {
        Employee employee = new Employee();
        employee.setFullName(dto.getFullName());
        employee.setEmail(dto.getEmail());
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
        dto.setRole(employee.getRole()); // use getRole to return enum value
        dto.setOffice(employee.getOffice());
        dto.setOnLeave(employee.isOnLeave());
        return dto;
    }
}
