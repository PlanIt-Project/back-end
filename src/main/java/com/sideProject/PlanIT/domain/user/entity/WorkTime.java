package com.sideProject.PlanIT.domain.user.entity;

import jakarta.persistence.*;

import java.time.LocalDateTime;

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
}
