package com.clock_in.clock.service;

import com.clock_in.clock.model.Employee;
import com.clock_in.clock.repository.EmployeeRepository;
import com.clock_in.clock.dto.EmployeeRequestDTO;
import com.clock_in.clock.dto.EmployeeResponseDTO;
import com.clock_in.core.exceptions.EmailAlreadyExists;
import com.clock_in.core.exceptions.EmployeeNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.EmptyStackException;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class EmployeeService {

    private final EmployeeRepository employeeRepository;

    // Constructor injection of the repository
    public EmployeeService(EmployeeRepository employeeRepository) {
        this.employeeRepository = employeeRepository;
    }

    // ----------------------------
    // Get employee by UUID (internal use)
    // ----------------------------
    public Employee getEmployeeByUuid(UUID uuid) throws EmployeeNotFoundException {
        return employeeRepository.findByUuid(uuid)
                .orElseThrow(EmployeeNotFoundException::new);
    }

    // ----------------------------
    // Create a new employee
    // ----------------------------
    @Transactional
    public EmployeeResponseDTO createEmployee(EmployeeRequestDTO request) throws EmailAlreadyExists {

        // Validate unique email
        if (employeeRepository.existsByEmail(request.getEmail())) {
            throw new EmailAlreadyExists();
        }

        Employee employee = new Employee();
        employee.setFullName(request.getFullName());
        employee.setEmail(request.getEmail());
        // In production, hash the password properly
        employee.setPasswordHash(request.getPassword());
        employee.setOffice(request.getOffice());
        employee.setRole(request.getRole());
        employee.setOnLeave(request.isOnLeave());

        employeeRepository.save(employee);

        return EmployeeResponseDTO.fromEntity(employee);
    }

    // ----------------------------
    // Get a single employee by UUID
    // ----------------------------
    public EmployeeResponseDTO getEmployee(String uuid) {
        Employee employee = employeeRepository.findByUuid(UUID.fromString(uuid))
                .orElseThrow(EmptyStackException::new);
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
    // Update employee info
    // ----------------------------
    @Transactional
    public EmployeeResponseDTO updateEmployee(String uuid, EmployeeRequestDTO request) throws EmployeeNotFoundException {
        Employee employee = employeeRepository.findByUuid(UUID.fromString(uuid))
                .orElseThrow(EmployeeNotFoundException::new);

        employee.setFullName(request.getFullName());
        employee.setEmail(request.getEmail());
        // Only update password if provided
        if (request.getPassword() != null && !request.getPassword().isBlank()) {
            employee.setPasswordHash(request.getPassword());
        }
        employee.setOffice(request.getOffice());
        employee.setRole(request.getRole());
        employee.setOnLeave(request.isOnLeave());

        employeeRepository.save(employee);

        return EmployeeResponseDTO.fromEntity(employee);
    }

    // ----------------------------
    // Optional: delete an employee
    // ----------------------------
    @Transactional
    public void deleteEmployee(String uuid) throws EmployeeNotFoundException {
        Employee employee = employeeRepository.findByUuid(UUID.fromString(uuid))
                .orElseThrow(EmployeeNotFoundException::new);
        employeeRepository.delete(employee);
    }
}
