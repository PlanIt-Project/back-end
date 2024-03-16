package com.sideProject.PlanIT.domain.user.dto.member.request;

import lombok.Getter;

@Getter
public class MemberSignInRequestDto {
    private String email;
    private String password;
}
