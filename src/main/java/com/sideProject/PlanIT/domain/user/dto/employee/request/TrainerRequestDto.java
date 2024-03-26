package com.sideProject.PlanIT.domain.user.dto.employee.request;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class TrainerRequestDto {
    private String career;
    private String trainerMessage;

    @Builder
    public TrainerRequestDto(String career, String trainerMessage) {
        this.career = career;
        this.trainerMessage = trainerMessage;
    }
}
