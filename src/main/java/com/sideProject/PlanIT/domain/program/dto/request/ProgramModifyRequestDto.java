package com.sideProject.PlanIT.domain.program.dto.request;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class ProgramModifyRequestDto {

    @NotNull(message = "시작시간이 없습니다.")
    String startTime;
    @NotNull(message = "시작시간이 없습니다.")
    String endTime;
    @NotNull(message = "회원 정보가 없습니다.")
    Long memberId;
    @NotNull(message = "트레이너 정보가 없습니다.")
    Long employId;

    @Builder
    public ProgramModifyRequestDto(String startTime, String endTime, Long memberId, Long employId) {
        this.startTime = startTime;
        this.endTime = endTime;
        this.memberId = memberId;
        this.employId = employId;
    }
}
