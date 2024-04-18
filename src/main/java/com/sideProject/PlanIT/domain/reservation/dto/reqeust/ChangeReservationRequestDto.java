package com.sideProject.PlanIT.domain.reservation.dto.reqeust;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Getter
@NoArgsConstructor
public class ChangeReservationRequestDto {
    @NotNull(message = "예약 날짜가 주어지지 않았습니다.")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    LocalDate reservationDate;

    @DateTimeFormat(pattern = "HH:mm")
    List<LocalTime> reservationTimes;

    @Builder
    public ChangeReservationRequestDto(LocalDate reservationDate, List<LocalTime> reservationTimes) {
        this.reservationDate = reservationDate;
        this.reservationTimes = reservationTimes;
    }
}
