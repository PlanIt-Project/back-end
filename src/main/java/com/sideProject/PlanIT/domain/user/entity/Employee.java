package com.sideProject.PlanIT.domain.user.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Employee {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column
    private String career;

    @Column
    private String trainerMessage;

    @OneToOne
    @JoinColumn(name = "member_id")
    private Member member;

    @Builder
    public Employee(String career, String trainerMessage, Member member) {
        this.career = career;
        this.trainerMessage = trainerMessage;
        this.member = member;
    }
}
