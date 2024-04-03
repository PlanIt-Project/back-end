package com.sideProject.PlanIT.domain.program.dto.request;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class ApproveRequestDto {
    Long trainer;

    @Builder
    public ApproveRequestDto(Long trainer) {
        this.trainer = trainer;
    }
}
