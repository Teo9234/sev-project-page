package com.clock_in.clock.repository;

import com.clock_in.clock.model.Employee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface EmployeeRepository extends JpaRepository<Employee, Long>,
        JpaSpecificationExecutor<Employee> {

    // Find by email (used for login, validation)
    Optional<Employee> findByEmail(String email);

    // Find by full name (used for search, admin functions)
    Optional<Employee> findByFullName(String fullName);

    // Find all employees currently on leave (HR, admin use)
    List<Employee> findByIsOnLeave(boolean isOnLeave);

    // Find all employees in a specific office (admin use, reporting)
    List<Employee> findByOffice(String office);

    // Find all employees currently working (clocked in but not out)
    List<Employee> findByClockEntries_ClockOutTimeIsNull();

    // Find by UUID (used for API endpoints, internal logic)
    Optional<Employee> findByUuid(UUID uuid);

    // Fast existence check by email (used for validation before create/update)
    boolean existsByEmail(String email);
}
