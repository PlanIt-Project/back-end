package com.sideProject.PlanIT.domain.user.dto.member.request;

import com.sideProject.PlanIT.domain.user.entity.ENUM.MemberRole;
import lombok.AccessLevel;
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
}
