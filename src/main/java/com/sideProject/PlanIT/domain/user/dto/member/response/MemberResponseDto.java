package com.sideProject.PlanIT.domain.user.dto.member.response;

import com.sideProject.PlanIT.domain.user.entity.ENUM.MemberRole;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;

@Getter
public class MemberResponseDto {
    private String email;
    private String name;
    private String phone_number;
    private LocalDate birth;
    private String address;
    private MemberRole role;

    @Builder
    public MemberResponseDto(String email, String name, String phone_number, LocalDate birth, String address, MemberRole role) {
        this.email = email;
        this.name = name;
        this.phone_number = phone_number;
        this.birth = birth;
        this.address = address;
        this.role = role;
    }
}
