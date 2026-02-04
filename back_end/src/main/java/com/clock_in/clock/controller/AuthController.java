package com.clock_in.clock.controller;

import com.clock_in.clock.dto.auth.LoginRequestDTO;
import com.clock_in.clock.dto.auth.LoginResponseDTO;
import com.clock_in.clock.dto.auth.RegisterRequestDTO;
import com.clock_in.clock.dto.auth.RegisterResponseDTO;
import com.clock_in.clock.mapper.AuthMapper;
import com.clock_in.clock.model.Employee;
import com.clock_in.clock.repository.EmployeeRepository;
import com.clock_in.core.exceptions.AppGenericException;
import com.clock_in.core.exceptions.ValidationException;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.Optional;

@RestController
@RequestMapping("/api/auth")
@Validated
public class AuthController {

    private final EmployeeRepository employeeRepository;
    private final PasswordEncoder passwordEncoder;

    public AuthController(EmployeeRepository employeeRepository, PasswordEncoder passwordEncoder) {
        this.employeeRepository = employeeRepository;
        this.passwordEncoder = passwordEncoder;
    }

    // -----------------------------
    // Registration
    // -----------------------------
    @PostMapping("/register")
    public ResponseEntity<RegisterResponseDTO> register(
            @Valid @RequestBody RegisterRequestDTO request
    ) throws AppGenericException {

        // Check for duplicate email
        if (employeeRepository.findByEmail(request.getEmail()).isPresent()) {
            throw new AppGenericException("Email already in use", "EMAIL_DUPLICATE");
        }

        // Map DTO to entity
        Employee employee = AuthMapper.toEmployeeEntity(request, passwordEncoder);

        // Save employee
        employeeRepository.save(employee);

        // Map back to response DTO
        RegisterResponseDTO response = AuthMapper.toRegisterResponseDTO(employee);

        return ResponseEntity.ok(response);
    }

    // -----------------------------
    // Login
    // -----------------------------
    @PostMapping("/login")
    public ResponseEntity<LoginResponseDTO> login(
            @Valid @RequestBody LoginRequestDTO request
    ) throws AppGenericException {

        Employee employee = employeeRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new AppGenericException("Invalid email or password", "LOGIN_FAILED"));

        // Verify password
        if (!passwordEncoder.matches(request.getPassword(), employee.getPasswordHash())) {
            throw new AppGenericException("Invalid email or password", "LOGIN_FAILED");
        }

        // Normally you would generate a JWT token here
        LoginResponseDTO response = new LoginResponseDTO();
        response.setToken("dummy-jwt-token"); // Replace with real JWT generation
        response.setFullName(employee.getFullName());
        response.setEmail(employee.getEmail());
        response.setRole(String.valueOf(employee.getRole()));

        return ResponseEntity.ok(response);
    }
}
