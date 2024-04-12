package com.sideProject.PlanIT.domain.user.dto.employee.request;

import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class TrainerRequestDto {
    @NotNull(message = "경력사항이 작성되지 않았습니다.")
    private String career;
    private String trainerMessage;

    @Builder
    public TrainerRequestDto(String career, String trainerMessage) {
        this.career = career;
        this.trainerMessage = trainerMessage;
    }
}
