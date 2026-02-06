package com.clock_in.clock.controller;

import com.clock_in.clock.dto.EmployeeRequestDTO;
import com.clock_in.clock.dto.EmployeeResponseDTO;
import com.clock_in.clock.dto.auth.RegisterRequestDTO;
import com.clock_in.clock.service.EmployeeService;
import com.clock_in.clock.validator.ValidUUID;
import com.clock_in.core.exceptions.EmailAlreadyExists;
import com.clock_in.core.exceptions.EmployeeNotFoundException;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/employees")
public class EmployeeController {

    private final EmployeeService employeeService;

    public EmployeeController(EmployeeService employeeService) {
        this.employeeService = employeeService;
    }

    // ----------------------------
    // Create a new employee
    // POST /api/employees
    // ----------------------------
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public EmployeeResponseDTO createEmployee(@Valid @RequestBody RegisterRequestDTO request) throws EmailAlreadyExists {
        return employeeService.createEmployee(request);
    }

    // ----------------------------
    // Get employee by UUID
    // GET /api/employees/{uuid}
    // ----------------------------
    @GetMapping("/{uuid}")
    public EmployeeResponseDTO getEmployee(@ValidUUID @PathVariable String uuid) throws EmployeeNotFoundException {
        return employeeService.getEmployee(uuid);
    }

    // ----------------------------
    // List all employees
    // GET /api/employees
    // ----------------------------
    @GetMapping
    public List<EmployeeResponseDTO> listEmployees() {
        return employeeService.listEmployees();
    }

    // ----------------------------
    // Update employee
    // PUT /api/employees/{uuid}
    // ----------------------------
    @PutMapping("/{uuid}")
    public EmployeeResponseDTO updateEmployee(
            @ValidUUID @PathVariable String uuid,
            @Valid @RequestBody EmployeeRequestDTO request
    ) throws EmployeeNotFoundException {
        return employeeService.updateEmployee(uuid, request);
    }

    // ----------------------------
    // Delete employee
    // DELETE /api/employees/{uuid}
    // ----------------------------
    @DeleteMapping("/{uuid}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteEmployee(@PathVariable String uuid) throws EmployeeNotFoundException {
        employeeService.deleteEmployee(uuid);
    }
}
