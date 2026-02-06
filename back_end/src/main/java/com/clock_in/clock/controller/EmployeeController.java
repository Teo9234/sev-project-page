package com.clock_in.clock.controller;

import com.clock_in.clock.dto.EmployeeRequestDTO;
import com.clock_in.clock.dto.EmployeeResponseDTO;
import com.clock_in.clock.dto.PagedEmployeeResponseDTO;
import com.clock_in.clock.dto.auth.RegisterRequestDTO;
import com.clock_in.clock.service.EmployeeService;
import com.clock_in.clock.validator.ValidUUID;
import com.clock_in.core.exceptions.EmailAlreadyExists;
import com.clock_in.core.exceptions.EmployeeNotFoundException;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/employees")
@Validated
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
    // List all employees (legacy, no pagination)
    // GET /api/employees
    // ----------------------------
    @GetMapping
    public List<EmployeeResponseDTO> listEmployees() {
        return employeeService.listEmployees();
    }

    // ----------------------------
    // Search employees with pagination, search & filters
    // GET /api/employees/search?search=&office=&role=&onLeave=&page=0&size=10&sortBy=fullName&sortDir=asc
    // ----------------------------
    @GetMapping("/search")
    public PagedEmployeeResponseDTO searchEmployees(
            @RequestParam(required = false) String search,
            @RequestParam(required = false) String office,
            @RequestParam(required = false) String role,
            @RequestParam(required = false) Boolean onLeave,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "fullName") String sortBy,
            @RequestParam(defaultValue = "asc") String sortDir
    ) {
        return employeeService.searchEmployees(search, office, role, onLeave, page, size, sortBy, sortDir);
    }

    // ----------------------------
    // Update employee (MANAGER or SUPER_ADMIN only)
    // PUT /api/employees/{uuid}
    // ----------------------------
    @PutMapping("/{uuid}")
    @PreAuthorize("hasAnyRole('MANAGER', 'SUPER_ADMIN')")
    public EmployeeResponseDTO updateEmployee(
            @ValidUUID @PathVariable String uuid,
            @Valid @RequestBody EmployeeRequestDTO request
    ) throws EmployeeNotFoundException {
        return employeeService.updateEmployee(uuid, request);
    }

    // ----------------------------
    // Delete employee (MANAGER or SUPER_ADMIN only)
    // DELETE /api/employees/{uuid}
    // ----------------------------
    @DeleteMapping("/{uuid}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PreAuthorize("hasAnyRole('MANAGER', 'SUPER_ADMIN')")
    public void deleteEmployee(@PathVariable String uuid) throws EmployeeNotFoundException {
        employeeService.deleteEmployee(uuid);
    }
}