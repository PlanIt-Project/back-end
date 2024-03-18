package com.sideProject.PlanIT.domain.user.dto.employee.response;

import com.sideProject.PlanIT.domain.user.entity.ENUM.MemberRole;
import com.sideProject.PlanIT.domain.user.entity.Employee;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

// 특정 트레이너 조회 시 사용
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class TrainerResponseDto {
    private Long id;
    private String email;
    private String name;
    private String phone_number;
    private LocalDate birth;
    private String address;
    private MemberRole role;
    private String career;
    private String trainerMessage;

    @Builder
    public TrainerResponseDto(Long id, String email, String name, String phone_number, LocalDate birth, String address, MemberRole role, String career, String trainerMessage) {
        this.id = id;
        this.email = email;
        this.name = name;
        this.phone_number = phone_number;
        this.birth = birth;
        this.address = address;
        this.role = role;
        this.career = career;
        this.trainerMessage = trainerMessage;
    }

    public static TrainerResponseDto of(Employee employee) {
        return TrainerResponseDto.builder()
                .id(employee.getId())
                .email(employee.getMember().getEmail())
                .name(employee.getMember().getName())
                .phone_number(employee.getMember().getPhone_number())
                .birth(employee.getMember().getBirth())
                .address(employee.getMember().getAddress())
                .role(employee.getMember().getRole())
                .career(employee.getCareer())
                .trainerMessage(employee.getTrainerMessage())
                .build();
    }
}
