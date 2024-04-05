package com.sideProject.PlanIT.domain.user.dto.employee.request;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalTime;


@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)

public class TrainerScheduleChangeRequestDto {
    private LocalTime startAt;
    private LocalTime endAt;


}
