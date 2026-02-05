package com.clock_in.clock.service;

import com.clock_in.clock.model.Employee;
import com.clock_in.clock.repository.EmployeeRepository;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class JwtService {

    private final String secret = System.getenv("JWT_SECRET_KEY");
    private final long expirationMillis = 86400000; // 24 hours in milliseconds
    private final EmployeeRepository employeeRepository;

    public JwtService(EmployeeRepository employeeRepository) {
        this.employeeRepository = employeeRepository;
    }

    public String generateToken(Employee employee) {
        Claims claims = Jwts.claims().setSubject(employee.getEmail());
        claims.put("role", employee.getRole().name());

        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + expirationMillis))
                .signWith(SignatureAlgorithm.HS512, secret)
                .compact();
    }

    public boolean isTokenValid(String token) {
        try {
            Jwts.parserBuilder().setSigningKey(secret).build().parseClaimsJws(token);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public String extractEmail(String token) {
        Claims claims = Jwts.parserBuilder().setSigningKey(secret).build().parseClaimsJws(token).getBody();
        return claims.getSubject(); // Extracts the email from the token
    }

    public Employee getEmployeeFromToken(String token) {
        String email = extractEmail(token);
        return employeeRepository.findByEmail(email).orElse(null); // Fetch employee from DB
    }
}
