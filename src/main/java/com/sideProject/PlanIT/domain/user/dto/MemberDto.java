package com.sideProject.PlanIT.domain.user.dto;

import com.sideProject.PlanIT.domain.user.entity.ENUM.MemberRole;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;

public class MemberDto {
    @Getter
    public static class MemberSignUpRequestDto {
        private String email;
        private String password;
        private String name;
        private String phone_number;
        private LocalDate birth;
        private String address;
        private MemberRole role;
    }

    @Getter
    public static class MemberEditRequestDto {
        private String email;
        private String name;
        private String phone_number;
        private LocalDate birth;
        private String address;
    }

    @Getter
    public static class MemberChangePasswordRequestDto {
        private String currentPassword;
        private String newPassword;
        private String newPasswordCheck;
    }

    @Builder
    @Getter
    public static class MemberResponseDto {
        private String email;
        private String name;
        private String phone_number;
        private LocalDate birth;
        private String address;
        private MemberRole role;
    }
}
