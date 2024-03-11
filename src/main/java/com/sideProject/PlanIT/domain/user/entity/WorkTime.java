package com.sideProject.PlanIT.domain.user.entity;

import com.sideProject.PlanIT.common.baseentity.StartToEndTimeBaseEntity;
import jakarta.persistence.*;

public class WorkTime extends StartToEndTimeBaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long Id;

    @Column
    private String week; // 요일

    @ManyToOne
    @JoinColumn(name = "employee_id")
    Employee employee;

}
