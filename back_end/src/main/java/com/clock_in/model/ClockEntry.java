package com.clock_in.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "clock_entries")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class ClockEntry extends AbstractEntity {

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "employee_id", nullable = false)
    private Employee employee;

    @Column(nullable = false)
    private LocalDateTime clockInTime;

    @Column
    private LocalDateTime clockOutTime;

    // Optional: helper method to check if this entry is active
    public boolean isActive() {
        return clockOutTime == null;
    }
}
