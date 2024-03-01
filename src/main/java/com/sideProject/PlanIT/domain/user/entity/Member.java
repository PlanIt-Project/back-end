package com.sideProject.PlanIT.domain.user.entity;

import com.sideProject.PlanIT.domain.user.entity.ENUM.MemberRole;
import jakarta.persistence.*;

import java.time.LocalDate;

@Entity
public class Member {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "member_id")
    private Long Id;

    @Column
    private String name;

    @Column
    private String email;

    @Column
    private String password;

    @Column
    private String phone_number;

    @Column
    private LocalDate birth;

    @Column
    private String address;

    @Enumerated(EnumType.STRING)
    private MemberRole role;
}
