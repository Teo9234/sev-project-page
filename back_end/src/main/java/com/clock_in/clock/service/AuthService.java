package com.clock_in.clock.service;

import com.clock_in.clock.dto.auth.LoginRequestDTO;
import com.clock_in.clock.dto.auth.LoginResponseDTO;
import com.clock_in.clock.dto.auth.RegisterRequestDTO;
import com.clock_in.clock.dto.auth.RegisterResponseDTO;
import com.clock_in.clock.mapper.AuthMapper;
import com.clock_in.clock.model.Employee;
import com.clock_in.clock.repository.EmployeeRepository;
import com.clock_in.core.exceptions.AppGenericException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    private final EmployeeRepository employeeRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    public AuthService(EmployeeRepository employeeRepository,
                       PasswordEncoder passwordEncoder,
                       JwtService jwtService) {
        this.employeeRepository = employeeRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
    }

    // -----------------------------
    // Register
    // -----------------------------
    public RegisterResponseDTO register(RegisterRequestDTO request) throws AppGenericException {
        if (employeeRepository.findByEmail(request.getEmail()).isPresent()) {
            throw new AppGenericException("Email already in use", "EMAIL_DUPLICATE");
        }

        Employee employee = AuthMapper.toEmployeeEntity(request, passwordEncoder);
        employeeRepository.save(employee);

        return AuthMapper.toRegisterResponseDTO(employee);
    }

    // -----------------------------
    // Login
    // -----------------------------
    public LoginResponseDTO login(LoginRequestDTO request) throws AppGenericException {
        Employee employee = employeeRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new AppGenericException("Invalid email or password", "LOGIN_FAILED"));

        if (!passwordEncoder.matches(request.getPassword(), employee.getPasswordHash())) {
            throw new AppGenericException("Invalid email or password", "LOGIN_FAILED");
        }

        String token = jwtService.generateToken(employee);

        LoginResponseDTO response = new LoginResponseDTO();
        response.setToken(token);
        response.setFullName(employee.getFullName());
        response.setEmail(employee.getEmail());
        response.setRole(employee.getRole().name());
        response.setOffice(employee.getOffice());
        response.setOnLeave(employee.isOnLeave());

        return response;
    }
}
