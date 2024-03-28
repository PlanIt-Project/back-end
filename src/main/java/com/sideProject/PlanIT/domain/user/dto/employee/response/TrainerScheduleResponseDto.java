package com.sideProject.PlanIT.domain.user.dto.employee.response;

import com.sideProject.PlanIT.domain.user.entity.WorkTime;
import lombok.Builder;
import lombok.Getter;
import java.time.LocalTime;

@Getter
public class TrainerScheduleResponseDto {
    Long schedule_id;
    String week;
    LocalTime startAt;
    LocalTime endAt;
    @Builder
    public TrainerScheduleResponseDto(Long schedule_id, String week, LocalTime startAt, LocalTime endAt){
        this.schedule_id = schedule_id;
        this.week = week;
        this.startAt = startAt;
        this.endAt = endAt;

    }

    public static TrainerScheduleResponseDto of(WorkTime worktime){
        return TrainerScheduleResponseDto.builder().schedule_id(worktime.getId()).week(worktime.getWeek()).startAt(worktime.getStartAt()).endAt(worktime.getEndAt()).build();
    }
}
