package com.clock_in.clock.specification;

import com.clock_in.clock.model.Employee;
import com.clock_in.core.enums.Role;
import org.springframework.data.jpa.domain.Specification;

public class EmployeeSpecification {

    // --------------------------------
    // Search: partial match on name or email (case-insensitive)
    // --------------------------------
    public static Specification<Employee> searchByKeyword(String keyword) {
        return (root, query, builder) -> {
            String pattern = "%" + keyword.toLowerCase() + "%";
            return builder.or(
                    builder.like(builder.lower(root.get("fullName")), pattern),
                    builder.like(builder.lower(root.get("email")), pattern)
            );
        };
    }

    // Filter by email (exact)
    public static Specification<Employee> hasEmail(String email) {
        return (root, query, builder) ->
                builder.equal(root.get("email"), email);
    }

    // Filter by full name (exact)
    public static Specification<Employee> hasFullName(String fullName) {
        return (root, query, builder) ->
                builder.equal(root.get("fullName"), fullName);
    }

    // Filter by office
    public static Specification<Employee> hasOffice(String office) {
        return (root, query, builder) ->
                builder.equal(root.get("office"), office);
    }

    // Filter by leave status
    public static Specification<Employee> isOnLeave(boolean onLeave) {
        return (root, query, builder) ->
                builder.equal(root.get("isOnLeave"), onLeave);
    }

    // Filter by role
    public static Specification<Employee> hasRole(Role role) {
        return (root, query, builder) ->
                builder.equal(root.get("role"), role);
    }
}