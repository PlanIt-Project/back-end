package com.sideProject.PlanIT.domain.user.dto.member.request;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class MemberSignUpRequestDto {
    private String email;
    private String password;
    private String name;
    private String phone_number;
    private LocalDate birth;
    private String address;

    @Builder
    public MemberSignUpRequestDto(String email, String password, String name, String phone_number, LocalDate birth, String address) {
        this.email = email;
        this.password = password;
        this.name = name;
        this.phone_number = phone_number;
        this.birth = birth;
        this.address = address;
    }
}
