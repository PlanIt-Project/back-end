package com.sideProject.PlanIT.domain.user.dto.member.request;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class MemberEditRequestDto {
    private String name;
    private String phone_number;
    private LocalDate birth;
    private String address;

    @Builder
    public MemberEditRequestDto(String name, String phone_number, LocalDate birth, String address) {
        this.name = name;
        this.phone_number = phone_number;
        this.birth = birth;
        this.address = address;
    }
}
