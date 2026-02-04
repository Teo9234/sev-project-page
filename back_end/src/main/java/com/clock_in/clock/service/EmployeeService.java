package com.clock_in.clock.service;

import com.clock_in.clock.model.Employee;
import com.clock_in.clock.repository.EmployeeRepository;
import com.clock_in.clock.specification.EmployeeSpecification;
import com.clock_in.core.exceptions.AppGenericException;
import com.clock_in.core.exceptions.ValidationException;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class EmployeeService {

    private final EmployeeRepository employeeRepository;

    public EmployeeService(EmployeeRepository employeeRepository) {
        this.employeeRepository = employeeRepository;
    }

    // -----------------------------
    // Dynamic search using specifications
    // -----------------------------
    public List<Employee> searchEmployees(
            Optional<String> email,
            Optional<String> fullName,
            Optional<String> office,
            Optional<Boolean> isOnLeave
    ) throws ValidationException {

        Specification<Employee> spec = buildSpecification(email, fullName, office, isOnLeave);

        List<Employee> results = employeeRepository.findAll(spec);

        if (results.isEmpty()) {
            throw new ValidationException("No employees found with the given criteria", "EMP_NOT_FOUND");
        }

        return results;
    }

    // -----------------------------
    // Find by email
    // -----------------------------
    public Employee getByEmail(String email) throws AppGenericException {
        return employeeRepository.findByEmail(email)
                .orElseThrow(() -> new AppGenericException("Employee not found", "EMP_NOT_FOUND"));
    }

    // -----------------------------
    // Helper method to build specifications
    // -----------------------------
    private Specification<Employee> buildSpecification(
            Optional<String> email,
            Optional<String> fullName,
            Optional<String> office,
            Optional<Boolean> isOnLeave
    ) {
        Specification<Employee> spec = null;

        if (email.isPresent()) spec = addSpec(spec, EmployeeSpecification.hasEmail(email.get()));
        if (fullName.isPresent()) spec = addSpec(spec, EmployeeSpecification.hasFullName(fullName.get()));
        if (office.isPresent()) spec = addSpec(spec, EmployeeSpecification.hasOffice(office.get()));
        if (isOnLeave.isPresent()) spec = addSpec(spec, EmployeeSpecification.isOnLeave(isOnLeave.get()));

        return spec;
    }

    private Specification<Employee> addSpec(Specification<Employee> base, Specification<Employee> next) {
        return (base == null) ? next : base.and(next);
    }
}
