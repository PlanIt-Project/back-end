package com.sideProject.PlanIT.domain.user.entity;

import com.sideProject.PlanIT.domain.user.entity.ENUM.ScheduleStatus;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class EmployeeSchedule {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long Id;

    @Column
    private LocalDate date;

    @Column
    private LocalDateTime startAt;

    @Column
    private LocalDateTime endAt;

    @Enumerated(EnumType.STRING)
    private ScheduleStatus status;

    @ManyToOne
    @JoinColumn(name = "employee_id")
    private Employee employee;

    @Builder
    public EmployeeSchedule(LocalDate date, LocalDateTime startAt, LocalDateTime endAt, ScheduleStatus status, Employee employee) {
        this.date = date;
        this.startAt = startAt;
        this.endAt = endAt;
        this.status = status;
        this.employee = employee;
    }
}
