package com.sideProject.PlanIT.domain.user.entity.enums;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;

public enum Week {

    Mon(DayOfWeek.MONDAY),    // 월
    Tue(DayOfWeek.TUESDAY),   // 화
    wed(DayOfWeek.WEDNESDAY), // 수
    thu(DayOfWeek.THURSDAY),  // 목
    fri(DayOfWeek.FRIDAY),    // 금
    sat(DayOfWeek.SATURDAY),  // 토
    sun(DayOfWeek.SUNDAY);    // 일

    private final DayOfWeek dayOfWeek;

    Week(DayOfWeek dayOfWeek) {
        this.dayOfWeek = dayOfWeek;
    }

    public DayOfWeek getDayOfWeek() {
        return this.dayOfWeek;
    }

    public static Week from(LocalDate dateTime) {
        DayOfWeek dayOfWeek = dateTime.getDayOfWeek();
        for (Week week : Week.values()) {
            if (week.getDayOfWeek() == dayOfWeek) {
                return week;
            }
        }
        throw new IllegalArgumentException("No Week enum for DayOfWeek: " + dayOfWeek);
    }
}
