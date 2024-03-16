package com.sideProject.PlanIT.domain.user.dto.employee.response;

import com.sideProject.PlanIT.domain.user.entity.Employee;
import lombok.Builder;
import lombok.Getter;

@Getter
public class TrainerResponse {
    private Long id;
    private String career;

    @Builder
    public TrainerResponse(Long id, String career) {
        this.id = id;
        this.career = career;
    }

    public static TrainerResponse of(Employee employee) {
        return TrainerResponse.builder()
                .id(employee.getId())
                .career(employee.getCareer())
                .build();
    }
}
