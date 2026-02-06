package com.clock_in.core.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Map;
import java.util.stream.Collectors;

@RestControllerAdvice
public class GlobalExceptionHandler {

    // -----------------------------
    // Handle email duplicates
    // -----------------------------
    @ExceptionHandler(EmailAlreadyExists.class)
    public ResponseEntity<Map<String, String>> handleEmailExists(EmailAlreadyExists ex) {
        return ResponseEntity
                .status(HttpStatus.CONFLICT)
                .body(Map.of(
                        "error", ex.getMessage(),
                        "code", "EMAIL_ALREADY_EXISTS"
                ));
    }

    // -----------------------------
    // Handle employee not found
    // -----------------------------
    @ExceptionHandler(EmployeeNotFoundException.class)
    public ResponseEntity<Map<String, String>> handleNotFound(EmployeeNotFoundException ex) {
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(Map.of(
                        "error", ex.getMessage(),
                        "code", "EMPLOYEE_NOT_FOUND"
                ));
    }

    // -----------------------------
    // Handle validation errors (@Valid)
    // -----------------------------
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> handleValidationErrors(MethodArgumentNotValidException ex) {
        Map<String, String> errors = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .collect(Collectors.toMap(
                        FieldError::getField,
                        // if null, return "Invalid value" as default message
                        FieldError::getDefaultMessage
                ));

        return ResponseEntity
                .badRequest()
                .body(errors);
    }

    // -----------------------------
    // Handle generic app exceptions
    // -----------------------------
    @ExceptionHandler(AppGenericException.class)
    public ResponseEntity<Map<String, String>> handleGeneric(AppGenericException ex) {
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(Map.of(
                        "error", ex.getMessage(),
                        "code", ex.getCode() // assumes your AppGenericException has getCode()
                ));
    }

    // -----------------------------
    // Catch-all for unexpected errors
    // -----------------------------
    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, String>> handleOther(Exception ex) {
        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(Map.of(
                        "error", "Unexpected error occurred"
                ));
    }
}
