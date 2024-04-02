package com.sideProject.PlanIT.domain.reservation.dto.reqeust;

import lombok.Getter;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Getter
public class ChangeReservationRequestDto {
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    LocalDate reservationDate;

    @DateTimeFormat(pattern = "HH:mm")
    List<LocalTime> reservationTimes;
}
