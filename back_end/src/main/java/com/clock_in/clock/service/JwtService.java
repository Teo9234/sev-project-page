package com.clock_in.clock.service;

import com.clock_in.clock.model.Employee;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;

@Service
public class JwtService {

    private SecretKey secretKey;

    @Value("${jwt.secret}")
    private String secret; // from application.properties

    @Value("${jwt.expiration}")
    private long expirationMillis; // e.g., 86400000 for 24h

    @PostConstruct
    public void init() {
        // Initialize the SecretKey after Spring injects the secret
        secretKey = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
    }

    // -----------------------------
    // Generate JWT token for Employee
    // -----------------------------
    public String generateToken(Employee employee) {
        return Jwts.builder()
                .setSubject(employee.getUuid().toString())
                .claim("email", employee.getEmail())
                .claim("role", employee.getRole().name())
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + expirationMillis))
                .signWith(secretKey, SignatureAlgorithm.HS256)
                .compact();
    }

    // -----------------------------
    // Extract all claims
    // -----------------------------
    public Claims extractAllClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(secretKey)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    // -----------------------------
    // Validate token
    // -----------------------------
    public boolean isTokenValid(String token, Employee employee) {
        final String uuid = extractAllClaims(token).getSubject();
        return uuid.equals(employee.getUuid().toString()) && !isTokenExpired(token);
    }

    // -----------------------------
    // Check expiration
    // -----------------------------
    public boolean isTokenExpired(String token) {
        final Date expiration = extractAllClaims(token).getExpiration();
        return expiration.before(new Date());
    }

    // -----------------------------
    // Extract email from token
    // -----------------------------
    public String extractEmail(String token) {
        return extractAllClaims(token).get("email", String.class);
    }

    // -----------------------------
    // Extract role from token
    // -----------------------------
    public String extractRole(String token) {
        return extractAllClaims(token).get("role", String.class);
    }
}
