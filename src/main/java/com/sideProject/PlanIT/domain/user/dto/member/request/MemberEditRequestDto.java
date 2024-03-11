package com.sideProject.PlanIT.domain.user.dto.member.request;

import lombok.Getter;

import java.time.LocalDate;

@Getter
public class MemberEditRequestDto {
    private String email;
    private String name;
    private String phone_number;
    private LocalDate birth;
    private String address;
}
