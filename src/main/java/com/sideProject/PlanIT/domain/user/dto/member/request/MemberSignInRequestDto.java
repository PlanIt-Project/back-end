package com.sideProject.PlanIT.domain.user.dto.member.request;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class MemberSignInRequestDto {
    private String email;
    private String password;

    @Builder
    public MemberSignInRequestDto(String email, String password) {
        this.email = email;
        this.password = password;
    }
}
