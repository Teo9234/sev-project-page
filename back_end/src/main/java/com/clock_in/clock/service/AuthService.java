package com.clock_in.clock.service;

import com.clock_in.clock.dto.auth.LoginRequestDTO;
import com.clock_in.clock.dto.auth.LoginResponseDTO;
import com.clock_in.clock.dto.auth.RegisterRequestDTO;
import com.clock_in.clock.model.Employee;
import com.clock_in.clock.repository.EmployeeRepository;
import com.clock_in.clock.service.JwtService;
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

    // ----------------------------
    // REGISTER
    // ----------------------------
    public Employee register(RegisterRequestDTO request) throws EmailAlreadyExists {

        if (employeeRepository.findByEmail(request.getEmail()).isPresent()) {
            throw new EmailAlreadyExists();
        }

        Employee employee = new Employee();
        employee.setFullName(request.getFullName());
        employee.setEmail(request.getEmail());
        employee.setPasswordHash(passwordEncoder.encode(request.getPassword()));
        employee.setOffice(request.getOffice());
        employee.setRole(request.getRole()); // Enum
        employee.setOnLeave(false);

        return employeeRepository.save(employee);
    }

    // ----------------------------
    // LOGIN
    // ----------------------------
    public LoginResponseDTO login(LoginRequestDTO request) throws AppGenericException {

        Employee employee = employeeRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new AppGenericException("Invalid email or password", "LOGIN_FAILED"));

        boolean matches = passwordEncoder.matches(request.getPassword(), employee.getPasswordHash());
        if (!matches) {
            throw new AppGenericException("Invalid email or password", "LOGIN_FAILED");
        }

        String token = jwtService.generateToken(employee);

        return new LoginResponseDTO(employee.getUuid(), employee.getEmail(), employee.getFullName(), token);
    }
}
