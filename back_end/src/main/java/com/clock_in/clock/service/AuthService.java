package com.clock_in.clock.service;

import com.clock_in.clock.dto.auth.LoginRequestDTO;
import com.clock_in.clock.dto.auth.LoginResponseDTO;
import com.clock_in.clock.dto.auth.RegisterRequestDTO;
import com.clock_in.clock.dto.auth.RegisterResponseDTO;
import com.clock_in.clock.mapper.AuthMapper;
import com.clock_in.clock.model.Employee;
import com.clock_in.clock.repository.EmployeeRepository;
import com.clock_in.core.exceptions.AppGenericException;
import com.clock_in.core.exceptions.EmailAlreadyExists;
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

    /**
     * Register a new employee.
     * Returns RegisterResponseDTO, not Employee.
     */
    public RegisterResponseDTO register(RegisterRequestDTO request) throws EmailAlreadyExists {

        if (employeeRepository.existsByEmail(request.getEmail())) {
            throw new EmailAlreadyExists();
        }

        Employee employee = new Employee();
        employee.setFullName(request.getFullName());
        employee.setEmail(request.getEmail());
        employee.setPasswordHash(passwordEncoder.encode(request.getPassword()));
        employee.setOffice(request.getOffice());
        employee.setRole(request.getRole());
        employee.setOnLeave(request.isOnLeave());

        Employee saved = employeeRepository.save(employee);

        // Map Employee → RegisterResponseDTO
        return AuthMapper.toRegisterResponseDTO(saved);
    }

    /**
     * Login with email + password.
     * Returns LoginResponseDTO with JWT token.
     */
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
