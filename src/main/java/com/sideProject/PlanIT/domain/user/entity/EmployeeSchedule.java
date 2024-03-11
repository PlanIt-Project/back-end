package com.sideProject.PlanIT.domain.user.entity;

import com.sideProject.PlanIT.common.baseentity.StartToEndTimeBaseEntity;
import com.sideProject.PlanIT.domain.user.entity.ENUM.MemberRole;
import com.sideProject.PlanIT.domain.user.entity.ENUM.ScheduleStatus;
import jakarta.persistence.*;

import java.time.LocalDate;

@Entity
public class EmployeeSchedule extends StartToEndTimeBaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long Id;

    @Column
    private LocalDate date;

    @Enumerated(EnumType.STRING)
    private ScheduleStatus status;

    @ManyToOne
    @JoinColumn(name = "employee_id")
    private Employee employee;

}
