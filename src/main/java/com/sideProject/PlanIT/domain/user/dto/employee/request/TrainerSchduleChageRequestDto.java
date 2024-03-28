package com.sideProject.PlanIT.domain.user.dto.employee.request;

import lombok.Getter;

import java.time.LocalDateTime;
import java.time.LocalTime;

@Getter
public class TrainerSchduleChageRequestDto {
    private LocalTime startAt;
    private LocalTime endAt;


}
