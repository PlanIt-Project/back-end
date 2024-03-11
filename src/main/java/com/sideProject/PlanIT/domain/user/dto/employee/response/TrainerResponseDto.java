package com.sideProject.PlanIT.domain.user.dto.employee.response;

import com.sideProject.PlanIT.domain.user.entity.ENUM.MemberRole;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;

@Getter
public class TrainerResponseDto {
    private String email;
    private String name;
    private String phone_number;
    private LocalDate birth;
    private String address;
    private MemberRole role;
    private String career;

    @Builder
    public TrainerResponseDto(String email, String name, String phone_number, LocalDate birth, String address, MemberRole role, String career) {
        this.email = email;
        this.name = name;
        this.phone_number = phone_number;
        this.birth = birth;
        this.address = address;
        this.role = role;
        this.career = career;
    }
}
