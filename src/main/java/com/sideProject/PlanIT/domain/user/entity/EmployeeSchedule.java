package com.sideProject.PlanIT.domain.user.entity;

import jakarta.persistence.*;

import java.time.LocalDate;

@Entity
public class EmployeeSchedule {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long Id;

    @Column
    private LocalDate date;

    @Column
    private Boolean firstClass;

    @Column
    private Boolean secondClass;

    @Column
    private Boolean thirdClass;

    @Column
    private Boolean fourthClass;

    @Column
    private Boolean fifthClass;

    @Column
    private Boolean sixthClass;
}
