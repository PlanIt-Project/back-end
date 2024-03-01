package com.sideProject.PlanIT.domain.program.entity;

import com.sideProject.PlanIT.domain.product.entity.Product;
import com.sideProject.PlanIT.domain.program.entity.ENUM.RegisterationStatus;
import com.sideProject.PlanIT.domain.user.entity.Member;
import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
public class Registeration {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "registeration_id")
    private Long Id;

    @Column
    private LocalDateTime registerationAt;

    @Column
    private LocalDateTime paymentAt;

    @Column
    private LocalDateTime refundAt;

    @Enumerated(EnumType.STRING)
    RegisterationStatus status;

    @Column
    private int discount;

    @Column
    private int totalPrice;

    @ManyToOne
    @JoinColumn(name = "member_id")
    private Member member;

    @ManyToOne
    @JoinColumn(name = "product_id")
    private Product product;
}
