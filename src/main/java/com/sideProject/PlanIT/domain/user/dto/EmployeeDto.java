package com.sideProject.PlanIT.domain.user.dto;

import com.sideProject.PlanIT.domain.user.entity.ENUM.MemberRole;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;
import java.time.Period;

public class EmployeeDto {

    @Getter
    public static class TrainerRequestDto {
        private String career;
    }
    @Builder
    @Getter
    public static class TrainerResponseDto {
        private String email;
        private String name;
        private String phone_number;
        private LocalDate birth;
        private String address;
        private MemberRole role;
        private String career;
    }
}
