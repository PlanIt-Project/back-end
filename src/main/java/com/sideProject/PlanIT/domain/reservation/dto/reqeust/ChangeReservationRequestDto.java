package com.sideProject.PlanIT.domain.reservation.dto.reqeust;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Getter
public class ChangeReservationRequestDto {
    @NotNull(message = "예약 날짜가 주어지지 않았습니다.")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    LocalDate reservationDate;

    @DateTimeFormat(pattern = "HH:mm")
    List<LocalTime> reservationTimes;
}
