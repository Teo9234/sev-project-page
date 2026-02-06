package com.clock_in.clock.controller;

import com.clock_in.clock.dto.auth.LoginRequestDTO;
import com.clock_in.clock.dto.auth.LoginResponseDTO;
import com.clock_in.clock.dto.auth.RegisterRequestDTO;
import com.clock_in.clock.dto.auth.RegisterResponseDTO;
import com.clock_in.clock.service.AuthService;
import com.clock_in.core.exceptions.AppGenericException;
import com.clock_in.core.exceptions.EmailAlreadyExists;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    /**
     * Register a new employee
     */
    @PostMapping("/register")
    public ResponseEntity<RegisterResponseDTO> register(@Valid @RequestBody RegisterRequestDTO request) throws EmailAlreadyExists {
        RegisterResponseDTO response = authService.register(request);
        return ResponseEntity.ok(response);
    }

    /**
     * Login an employee
     */
    @PostMapping("/login")
    public ResponseEntity<LoginResponseDTO> login(@Valid @RequestBody LoginRequestDTO request) throws AppGenericException {
        LoginResponseDTO response = authService.login(request);
        return ResponseEntity.ok(response);
    }
}
