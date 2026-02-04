package com.clock_in.model;

import com.clock_in.core.enums.Role;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.AllArgsConstructor;
import org.hibernate.annotations.ColumnDefault;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "employees")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Employee extends AbstractEntity {

    @Column(nullable = false)
    private String fullName;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String passwordHash;

    @Column(nullable = false)
    private String office;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Role role;

    @Column(nullable = false)
    @ColumnDefault("false")
    private boolean isOnLeave;

    @OneToMany(
            mappedBy = "employee",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    private List<ClockEntry> clockEntries = new ArrayList<>();

    // Domain logic: check if currently working
    public boolean isCurrentlyWorking() {
        // Use helper method from ClockEntry for clarity
        return clockEntries.stream().anyMatch(ClockEntry::isActive);
    }
}
