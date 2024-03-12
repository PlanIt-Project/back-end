package com.sideProject.PlanIT.domain.user.dto.member.request;

import lombok.Getter;

@Getter
public class MemberChangePasswordRequestDto {
    private String currentPassword;
    private String newPassword;
    private String newPasswordCheck;
}
