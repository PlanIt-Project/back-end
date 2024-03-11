package com.sideProject.PlanIT.domain.user.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class WorkTime {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long Id;

    @Column
    private String week; // 요일

    @Column
    LocalDateTime startAt;

    @Column
    LocalDateTime endAt;

    @ManyToOne
    @JoinColumn(name = "employee_id")
    Employee employee;

    @Builder
    public WorkTime(String week, LocalDateTime startAt, LocalDateTime endAt, Employee employee) {
        this.week = week;
        this.startAt = startAt;
        this.endAt = endAt;
        this.employee = employee;
    }
}
