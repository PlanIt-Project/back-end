package com.sideProject.PlanIT.domain.reservation.dto.reqeust;

import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
public class ChangeReservationRequest {
    List<LocalDateTime> reservedTimes;
}
