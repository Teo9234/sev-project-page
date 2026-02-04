package com.clock_in.clock.specification;

import com.clock_in.clock.model.Employee;
import org.springframework.data.jpa.domain.Specification;

public class EmployeeSpecification {

    // Filter by email
    public static Specification<Employee> hasEmail(String email) {
        return (root, query, builder) ->
                builder.equal(root.get("email"), email);
    }

    // Filter by full name
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
}
