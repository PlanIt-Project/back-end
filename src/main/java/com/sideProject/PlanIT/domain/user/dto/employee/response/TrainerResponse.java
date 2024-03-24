package com.sideProject.PlanIT.domain.user.dto.employee.response;

import com.sideProject.PlanIT.domain.user.entity.Employee;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;


// 특정 멤버 조회 시 멤버 Dto에 포함
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class TrainerResponse {
    private Long id;
    private String career;
    private String trainerMessage;

    @Builder
    public TrainerResponse(Long id, String career, String trainerMessage) {
        this.id = id;
        this.career = career;
        this.trainerMessage = trainerMessage;
    }

    public static TrainerResponse of(Employee employee) {
        return TrainerResponse.builder()
                .id(employee.getId())
                .career(employee.getCareer())
                .trainerMessage(employee.getTrainerMessage())
                .build();
    }
}
