package com.sideProject.PlanIT.domain.user.dto.employee.request;

import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalTime;


@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class TrainerScheduleChangeRequestDto {

    @NotNull(message = "시작 시간이 없습니다")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalTime startAt;

    @NotNull(message = "종료 시간이 없습니다")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalTime endAt;


}
