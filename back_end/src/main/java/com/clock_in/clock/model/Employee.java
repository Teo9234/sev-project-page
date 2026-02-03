package com.clock_in.clock.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "employees")
public class Employee extends AbstractEntity{

    @Column(nullable = false)
    private String fullName;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String passwordHash;

    @Column(nullable = false)
    private String role;

    @Column(nullable = false)
    @ColumnDefault("false")
    private boolean isOnLeave;

    @OneToMany(
        mappedBy = "employee",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )

    private List<ClockEntry> clockEntries = new ArrayList<>();
}
