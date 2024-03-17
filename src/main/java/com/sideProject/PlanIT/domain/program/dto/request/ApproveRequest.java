package com.sideProject.PlanIT.domain.program.dto.request;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class ApproveRequest {
    Long trainer;

    @Builder
    public ApproveRequest(Long trainer) {
        this.trainer = trainer;
    }
}
