package com.clock_in.clock.service;

import com.clock_in.clock.dto.EmployeeRequestDTO;
import com.clock_in.clock.dto.EmployeeResponseDTO;
import com.clock_in.clock.dto.PagedEmployeeResponseDTO;
import com.clock_in.clock.dto.auth.RegisterRequestDTO;
import com.clock_in.clock.dto.auth.RegisterResponseDTO;
import com.clock_in.clock.mapper.AuthMapper;
import com.clock_in.clock.mapper.ClockMapper;
import com.clock_in.clock.model.Employee;
import com.clock_in.clock.repository.EmployeeRepository;
import com.clock_in.clock.specification.EmployeeSpecification;
import com.clock_in.core.enums.Role;
import com.clock_in.core.exceptions.EmailAlreadyExists;
import com.clock_in.core.exceptions.EmployeeNotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
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
    public EmployeeResponseDTO createEmployee(RegisterRequestDTO request) throws EmailAlreadyExists {

        if (employeeRepository.existsByEmail(request.getEmail())) {
            throw new EmailAlreadyExists();
        }

        Employee employee = AuthMapper.toEmployeeEntity(request);
        employee.setPasswordHash(passwordEncoder.encode(request.getPassword()));

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
    // Search employees with pagination, search & filters
    // ----------------------------
    public PagedEmployeeResponseDTO searchEmployees(
            String search,
            String office,
            String role,
            Boolean onLeave,
            int page,
            int size,
            String sortBy,
            String sortDir
    ) {
        Sort sort = sortDir.equalsIgnoreCase("desc")
                ? Sort.by(sortBy).descending()
                : Sort.by(sortBy).ascending();

        Pageable pageable = PageRequest.of(page, size, sort);

        Specification<Employee> spec = Specification.where(null);

        if (search != null && !search.isBlank()) {
            spec = spec.and(EmployeeSpecification.searchByKeyword(search.trim()));
        }
        if (office != null && !office.isBlank()) {
            spec = spec.and(EmployeeSpecification.hasOffice(office));
        }
        if (role != null && !role.isBlank()) {
            try {
                Role roleEnum = Role.valueOf(role.toUpperCase());
                spec = spec.and(EmployeeSpecification.hasRole(roleEnum));
            } catch (IllegalArgumentException ignored) {
                // Invalid role value, skip filter
            }
        }
        if (onLeave != null) {
            spec = spec.and(EmployeeSpecification.isOnLeave(onLeave));
        }

        Page<Employee> employeePage = employeeRepository.findAll(spec, pageable);

        List<EmployeeResponseDTO> content = employeePage.getContent()
                .stream()
                .map(EmployeeResponseDTO::fromEntity)
                .collect(Collectors.toList());

        return PagedEmployeeResponseDTO.from(
                content,
                employeePage.getNumber(),
                employeePage.getSize(),
                employeePage.getTotalElements(),
                employeePage.getTotalPages(),
                employeePage.isLast()
        );
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
            employee.setPasswordHash(passwordEncoder.encode(request.getPassword()));
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
