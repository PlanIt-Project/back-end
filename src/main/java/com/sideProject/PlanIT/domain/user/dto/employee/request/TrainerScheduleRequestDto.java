package com.sideProject.PlanIT.domain.user.dto.employee.request;

import com.sideProject.PlanIT.domain.user.entity.enums.Week;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalTime;

@Getter
@NoArgsConstructor
public class TrainerScheduleRequestDto {
    private Week week;
    private LocalTime startAt;
    private LocalTime endAt;
}
