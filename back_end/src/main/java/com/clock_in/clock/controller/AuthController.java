package com.clock_in.clock.controller;

import com.clock_in.clock.dto.auth.LoginRequestDTO;
import com.clock_in.clock.dto.auth.LoginResponseDTO;
import com.clock_in.clock.dto.auth.RegisterRequestDTO;
import com.clock_in.clock.dto.auth.RegisterResponseDTO;
import com.clock_in.clock.service.AuthService;
import com.clock_in.core.exceptions.AppGenericException;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
@Validated
@Tag(name = "Authentication", description = "Endpoints for user registration and login")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/register")
    public ResponseEntity<RegisterResponseDTO> register(@Valid
                                                            @RequestBody RegisterRequestDTO request) throws AppGenericException {
        RegisterResponseDTO response = authService.register(request);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponseDTO> login(@Valid
                                                      @RequestBody LoginRequestDTO request) throws AppGenericException {
        LoginResponseDTO response = authService.login(request);
        return ResponseEntity.ok(response);
    }
}
