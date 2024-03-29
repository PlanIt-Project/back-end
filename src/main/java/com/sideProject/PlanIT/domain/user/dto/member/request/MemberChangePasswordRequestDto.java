package com.sideProject.PlanIT.domain.user.dto.member.request;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class MemberChangePasswordRequestDto {
    private String currentPassword;
    private String newPassword;
    private String newPasswordCheck;

    @Builder
    public MemberChangePasswordRequestDto(String currentPassword, String newPassword, String newPasswordCheck) {
        this.currentPassword = currentPassword;
        this.newPassword = newPassword;
        this.newPasswordCheck = newPasswordCheck;
    }
}
