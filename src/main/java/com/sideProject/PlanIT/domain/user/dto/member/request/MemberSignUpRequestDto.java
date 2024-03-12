package com.sideProject.PlanIT.domain.user.dto.member.request;

import com.sideProject.PlanIT.domain.user.entity.ENUM.MemberRole;
import lombok.Getter;

import java.time.LocalDate;

@Getter
public class MemberSignUpRequestDto {
    private String email;
    private String password;
    private String name;
    private String phone_number;
    private LocalDate birth;
    private String address;
    private MemberRole role;
}
