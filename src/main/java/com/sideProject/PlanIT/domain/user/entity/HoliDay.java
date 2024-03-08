package com.sideProject.PlanIT.domain.user.entity;

import jakarta.persistence.*;

@Entity
public class HoliDay {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long Id;

    @ManyToOne
    @JoinColumn(name = "employee_id")
    private Employee employee;
}
