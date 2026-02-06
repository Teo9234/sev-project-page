package com.clock_in.clock.service;

import com.clock_in.clock.dto.EmployeeRequestDTO;
import com.clock_in.clock.dto.EmployeeResponseDTO;
import com.clock_in.clock.mapper.ClockMapper;
import com.clock_in.clock.model.Employee;
import com.clock_in.clock.repository.EmployeeRepository;
import com.clock_in.core.exceptions.EmailAlreadyExists;
import com.clock_in.core.exceptions.EmployeeNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class EmployeeService {

    private final EmployeeRepository employeeRepository;
    private final PasswordEncoder passwordEncoder;

    public EmployeeService(EmployeeRepository employeeRepository,
                           PasswordEncoder passwordEncoder) {
        this.employeeRepository = employeeRepository;
        this.passwordEncoder = passwordEncoder;
    }

    // ----------------------------
    // Get employee by UUID
    // ----------------------------
    public Employee getEmployeeByUuid(UUID uuid) throws EmployeeNotFoundException {
        return employeeRepository.findByUuid(uuid)
                .orElseThrow(EmployeeNotFoundException::new);
    }

    // ----------------------------
    // Create employee
    // ----------------------------
    @Transactional
    public EmployeeResponseDTO createEmployee(EmployeeRequestDTO request) throws EmailAlreadyExists {

        if (employeeRepository.existsByEmail(request.getEmail())) {
            throw new EmailAlreadyExists();
        }

        Employee employee = ClockMapper.toEmployeeEntity(request);
        employee.setPassword(passwordEncoder.encode(request.getPassword()));

        employeeRepository.save(employee);
        return EmployeeResponseDTO.fromEntity(employee);
    }

    // ----------------------------
    // Get employee by UUID as DTO
    // ----------------------------
    public EmployeeResponseDTO getEmployee(String uuid) throws EmployeeNotFoundException {
        Employee employee = getEmployeeByUuid(UUID.fromString(uuid));
        return EmployeeResponseDTO.fromEntity(employee);
    }

    // ----------------------------
    // List all employees
    // ----------------------------
    public List<EmployeeResponseDTO> listEmployees() {
        return employeeRepository.findAll()
                .stream()
                .map(EmployeeResponseDTO::fromEntity)
                .collect(Collectors.toList());
    }

    // ----------------------------
    // Update employee
    // ----------------------------
    @Transactional
    public EmployeeResponseDTO updateEmployee(String uuid, EmployeeRequestDTO request) throws EmployeeNotFoundException {
        Employee employee = getEmployeeByUuid(UUID.fromString(uuid));

        employee.setFullName(request.getFullName());
        employee.setEmail(request.getEmail());
        if (request.getPassword() != null && !request.getPassword().isBlank()) {
            employee.setPassword(passwordEncoder.encode(request.getPassword()));
        }
        employee.setOffice(request.getOffice());
        employee.setRole(request.getRole());
        employee.setOnLeave(request.isOnLeave());

        employeeRepository.save(employee);
        return EmployeeResponseDTO.fromEntity(employee);
    }

    // ----------------------------
    // Delete employee
    // ----------------------------
    @Transactional
    public void deleteEmployee(String uuid) throws EmployeeNotFoundException {
        Employee employee = getEmployeeByUuid(UUID.fromString(uuid));
        employeeRepository.delete(employee);
    }
}
