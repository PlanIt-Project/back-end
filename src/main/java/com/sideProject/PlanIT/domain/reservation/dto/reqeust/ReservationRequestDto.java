package com.sideProject.PlanIT.domain.reservation.dto.reqeust;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class ReservationRequestDto {
    @NotNull(message = "프로그램이 주어지지 않았습니다.")
    Long programId;

    @Builder
    public ReservationRequestDto(Long programId) {
        this.programId = programId;
    }
}
