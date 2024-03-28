package com.sideProject.PlanIT.domain.user.entity;

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

    @Column
    private String week; // 요일

    @Column
    LocalTime startAt;

    @Column
    LocalTime endAt;

    @ManyToOne
    @JoinColumn(name = "employee_id")
    Employee employee;

    @Builder
    public WorkTime(String week, LocalTime startAt, LocalTime endAt, Employee employee) {
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
