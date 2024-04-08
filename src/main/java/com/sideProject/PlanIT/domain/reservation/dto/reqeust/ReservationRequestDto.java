package com.sideProject.PlanIT.domain.reservation.dto.reqeust;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

@Getter
public class ReservationRequestDto {
    @NotNull(message = "프로그램이 주어지지 않았습니다.")
    Long programId;
}
