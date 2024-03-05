package com.sideProject.PlanIT.domain.program.entity;

import com.sideProject.PlanIT.domain.product.entity.Product;
import com.sideProject.PlanIT.domain.program.entity.ENUM.ProgramStatus;
import com.sideProject.PlanIT.domain.user.entity.Employee;
import com.sideProject.PlanIT.domain.user.entity.Member;
import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
public class Program {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "program_id")
    private Long Id;

    @Column
    private LocalDateTime createAt;

    @Column
    private LocalDateTime startAt;

    @Column
    private LocalDateTime endAt;

    @Enumerated(EnumType.STRING)
    ProgramStatus status;

    @ManyToOne
    @JoinColumn(name = "product_id")
    private Product product;

    @OneToOne
    @JoinColumn(name = "registeration_id")
    private Registeration registeration;

    @ManyToOne
    @JoinColumn(name = "member_id")
    private Member member;

    @ManyToOne
    @JoinColumn(name = "employee_id")
    private Employee employee;
}
