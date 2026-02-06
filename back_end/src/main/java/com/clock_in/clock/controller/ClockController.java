package com.clock_in.clock.controller;

import com.clock_in.clock.dto.ClockEntryResponseDTO;
import com.clock_in.clock.dto.ClockStatusResponseDTO;
import com.clock_in.clock.model.Employee;
import com.clock_in.clock.repository.EmployeeRepository;
import com.clock_in.clock.service.ClockService;
import com.clock_in.core.exceptions.AppGenericException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/clock")
public class ClockController {

    // Inject the ClockService and EmployeeRepository
    private final ClockService clockService;
    private final EmployeeRepository employeeRepository;


    // Constructor injection
    public ClockController(ClockService clockService, EmployeeRepository employeeRepository) {
        this.clockService = clockService;
        this.employeeRepository = employeeRepository;
    }

    /**
     * Get the current clock status for an employee
     * GET /api/clock/status?employeeUuid=...
     */
    @GetMapping("/status")
    public ResponseEntity<ClockStatusResponseDTO> getStatus(
            @RequestParam String employeeUuid
    ) throws AppGenericException {
        Employee employee = findEmployeeByUuid(employeeUuid);
        ClockStatusResponseDTO response = clockService.getStatus(employee);
        return ResponseEntity.ok(response);
    }

    /**
     * Clock in an employee
     * POST /api/clock/in?employeeUuid=...
     */
    @PostMapping("/in")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<ClockEntryResponseDTO> clockIn(
            @RequestParam String employeeUuid
    ) throws AppGenericException {

        // Lookup employee by UUID
        Employee employee = findEmployeeByUuid(employeeUuid);

        // Delegate to service
        ClockEntryResponseDTO response = ClockEntryResponseDTO.fromEntity(
                clockService.clockIn(employee)
        );

        return ResponseEntity.ok(response);
    }

    /**
     * Clock out an employee
     * POST /api/clock/out?employeeUuid=...
     */
    @PostMapping("/out")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<ClockEntryResponseDTO> clockOut(
            @RequestParam String employeeUuid
    ) throws AppGenericException {

        // Lookup employee by UUID
        Employee employee = findEmployeeByUuid(employeeUuid);

        // Delegate to service
        ClockEntryResponseDTO response = ClockEntryResponseDTO.fromEntity(
                clockService.clockOut(employee)
        );

        return ResponseEntity.ok(response);
    }

    // ----------------------------
    // Helper: find employee by UUID
    // ----------------------------
    private Employee findEmployeeByUuid(String uuid) throws AppGenericException {
        // You likely have EmployeeRepository
        return employeeRepository.findByUuid(java.util.UUID.fromString(uuid))
                .orElseThrow(() -> new AppGenericException(
                        "Employee not found",
                        "EMPLOYEE_NOT_FOUND"
                ));
    }

}
