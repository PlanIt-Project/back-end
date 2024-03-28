package com.sideProject.PlanIT.domain.user.dto.employee.request;

import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;

@Getter
@NoArgsConstructor
public class TrainerScheduleRequestDto {
    private ArrayList<String> week;
    private ArrayList<LocalTime> startAt;
    private ArrayList<LocalTime> endAt;
}
