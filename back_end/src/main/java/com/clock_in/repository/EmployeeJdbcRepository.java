package com.clock_in.repository;

import com.clock_in.clock.model.Employee;
import com.clock_in.core.enums.Role;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

@Repository
public class EmployeeJdbcRepository {

    private final JdbcTemplate jdbcTemplate;

    public EmployeeJdbcRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    // -------- RowMapper --------
    private final RowMapper<Employee> employeeRowMapper = new RowMapper<>() {
        @Override
        public Employee mapRow(ResultSet rs, int rowNum) throws SQLException {
            Employee e = new Employee();
            e.setId(rs.getLong("id"));
            e.setFullName(rs.getString("full_name"));
            e.setEmail(rs.getString("email"));
            e.setPasswordHash(rs.getString("password_hash"));
            e.setRole(Role.valueOf(rs.getString("role")));
            e.setOnLeave(rs.getBoolean("is_on_leave"));
            return e;
        }
    };

    // -------- Queries --------

    public Optional<Employee> findByEmail(String email) {
        String sql = "SELECT * FROM employees WHERE email = ?";
        return jdbcTemplate.query(sql, employeeRowMapper, email)
                .stream()
                .findFirst();
    }

    public Optional<Employee> findByFullName(String fullName) {
        String sql = "SELECT * FROM employees WHERE full_name = ?";
        return jdbcTemplate.query(sql, employeeRowMapper, fullName)
                .stream()
                .findFirst();
    }

    public List<Employee> findByIsOnLeave(boolean isOnLeave) {
        String sql = "SELECT * FROM employees WHERE is_on_leave = ?";
        return jdbcTemplate.query(sql, employeeRowMapper, isOnLeave);
    }

    public Optional<Employee> findByOffice(String office) {
        String sql = "SELECT * FROM employees WHERE office = ?";
        return jdbcTemplate.query(sql, employeeRowMapper, office)
                .stream()
                .findFirst();
    }

    /**
     * Equivalent of:
     * findByClockEntries_ClockOutTimeIsNull()
     */
    public List<Employee> findCurrentlyWorkingEmployees() {
        String sql = """
            SELECT DISTINCT e.*
            FROM employees e
            JOIN clock_entries c ON e.id = c.employee_id
            WHERE c.clock_out_time IS NULL
        """;

        return jdbcTemplate.query(sql, employeeRowMapper);
    }
}
