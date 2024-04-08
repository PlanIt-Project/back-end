package com.sideProject.PlanIT.domain.program.dto.request;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;

@Getter
public class ProgramModifyRequestDto {

    @NotNull
    String startTime;
    String endTime;
    Long memberId;
    Long employId;

    @Builder
    public ProgramModifyRequestDto(String startTime, String endTime, Long memberId, Long employId) {
        this.startTime = startTime;
        this.endTime = endTime;
        this.memberId = memberId;
        this.employId = employId;
    }
}
