package com.sideProject.PlanIT.domain.user.entity;

import com.sideProject.PlanIT.domain.user.entity.enums.Week;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;


import java.time.LocalTime;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class WorkTime {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Enumerated(EnumType.STRING)
    private Week week;

    @Column
    LocalTime startAt;

    @Column
    LocalTime endAt;

    @ManyToOne
    @JoinColumn(name = "employee_id")
    Employee employee;

    @Builder
    public WorkTime(Week week, LocalTime startAt, LocalTime endAt, Employee employee) {
        this.week = week;
        this.startAt = startAt;
        this.endAt = endAt;
        this.employee = employee;
    }

    public void ChageWorktime(LocalTime startAt, LocalTime endAt){
        this.startAt = startAt;
        this.endAt = endAt;
    }
}
