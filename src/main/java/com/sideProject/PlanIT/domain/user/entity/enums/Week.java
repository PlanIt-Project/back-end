package com.sideProject.PlanIT.domain.user.entity.enums;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;

public enum Week {

    Mon(DayOfWeek.MONDAY),    // 월
    Tue(DayOfWeek.TUESDAY),   // 화
    Wed(DayOfWeek.WEDNESDAY), // 수
    Thu(DayOfWeek.THURSDAY),  // 목
    Fri(DayOfWeek.FRIDAY),    // 금
    Sat(DayOfWeek.SATURDAY),  // 토
    Sun(DayOfWeek.SUNDAY);    // 일

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
