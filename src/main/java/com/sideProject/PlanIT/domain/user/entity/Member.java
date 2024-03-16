package com.sideProject.PlanIT.domain.user.entity;

import com.sideProject.PlanIT.domain.user.dto.member.request.MemberEditRequestDto;
import com.sideProject.PlanIT.domain.user.dto.member.response.MemberResponseDto;
import com.sideProject.PlanIT.domain.user.entity.ENUM.MemberRole;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Member {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long Id;

    @Column(name = "email", nullable = false, unique = true)
    private String email;

    @Column
    private String password;

    @Column
    private String name;

    @Column
    private String phone_number;

    @Column
    private LocalDate birth;

    @Column
    private String address;

    @Enumerated(EnumType.STRING)
    private MemberRole role;

    @Builder
    public Member(String name, String email, String password, String phone_number, LocalDate birth, String address, MemberRole role) {
        this.name = name;
        this.email = email;
        this.password = password;
        this.phone_number = phone_number;
        this.birth = birth;
        this.address = address;
        this.role = role;
    }

    public void update(MemberEditRequestDto memberEditRequestDto) {
        this.name = memberEditRequestDto.getName();
        this.birth = memberEditRequestDto.getBirth();
        this.address = memberEditRequestDto.getAddress();
        this.phone_number = memberEditRequestDto.getPhone_number();
    }

    public void changePassword(String newPassword) {
        this.password = newPassword;
    }

    public void  grantEmployeeAuth() {
        this.role = MemberRole.TRAINER;
    }

    public MemberResponseDto toDto() {
        return MemberResponseDto.builder()
                .email(this.email)
                .name(this.name)
                .birth(this.birth)
                .phone_number(this.phone_number)
                .address(this.address)
                .role(this.role)
                .build();
    }
}
