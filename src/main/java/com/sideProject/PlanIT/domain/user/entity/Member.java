package com.sideProject.PlanIT.domain.user.entity;

import com.sideProject.PlanIT.domain.user.dto.member.request.MemberEditRequestDto;
import com.sideProject.PlanIT.domain.user.entity.enums.Gender;
import com.sideProject.PlanIT.domain.user.entity.enums.MemberRole;
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
    private Long id;

    @Column(name = "email", nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
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
    private Gender gender;

    @Enumerated(EnumType.STRING)
    private MemberRole role;

    @Builder
    public Member(String name, String email, String password, String phone_number, LocalDate birth, String address, MemberRole role, Gender gender) {
        this.name = name;
        this.email = email;
        this.password = password;
        this.phone_number = phone_number;
        this.birth = birth;
        this.address = address;
        this.role = role;
        this.gender = gender;
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
}
