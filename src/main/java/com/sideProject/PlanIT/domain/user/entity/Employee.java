package com.sideProject.PlanIT.domain.user.entity;


import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
public class Employee {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "employee_id")
    private Long Id;

    @Column
    private LocalDateTime start_time;

    @Column
    private LocalDateTime end_time;

    //todo: 만약 요일별로 출퇴근 시간이 다르면?

    @OneToOne
    @JoinColumn(name = "member_id")
    private Member member;
}
