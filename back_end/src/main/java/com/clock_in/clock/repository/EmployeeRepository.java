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

        // Find employee by email
        Optional<Employee> findByEmail(String email);

        // Find employee by full name
        Optional<Employee> findByFullName(String fullName);

        // Lists employee by leave status
        List<Employee> findByIsOnLeave(boolean isOnLeave);

        // Lists employee by office
        List<Employee> findByOffice(String office);

        // Lists employee by isCurrentlyWorking status
        List<Employee> findByClockEntries_ClockOutTimeIsNull();

        // Find employee by uuid (if you have a UUID field in Employee, which is common for external references)
        Optional<Employee> findByUuid(UUID uuid);

        boolean existsByEmail(String email);

}
