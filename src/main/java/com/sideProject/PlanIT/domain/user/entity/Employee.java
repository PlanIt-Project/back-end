package com.sideProject.PlanIT.domain.user.entity;


import com.sideProject.PlanIT.domain.user.dto.employee.response.TrainerResponseDto;
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
    private Long Id;

    @Column
    private String career;

    @OneToOne
    @JoinColumn(name = "member_id")
    private Member member;

    @Builder
    public Employee(String career, Member member) {
        this.career = career;
        this.member = member;
    }

    public TrainerResponseDto toDto() {
        return TrainerResponseDto.builder()
                .email(this.member.getEmail())
                .name(this.member.getName())
                .birth(this.member.getBirth())
                .address(this.member.getAddress())
                .phone_number(this.member.getPhone_number())
                .role(this.member.getRole())
                .career(this.career)
                .build();
    }
}
