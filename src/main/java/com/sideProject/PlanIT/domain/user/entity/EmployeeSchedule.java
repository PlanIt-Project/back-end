package com.sideProject.PlanIT.domain.user.entity;

import com.sideProject.PlanIT.domain.user.entity.ENUM.ScheduleStatus;
import jakarta.persistence.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
public class EmployeeSchedule {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long Id;

    @Column
    private LocalDate date;

    @Column
    private LocalDateTime startAt;

    @Enumerated(EnumType.STRING)
    private ScheduleStatus status;

    @ManyToOne
    @JoinColumn(name = "employee_id")
    private Employee employee;

}
