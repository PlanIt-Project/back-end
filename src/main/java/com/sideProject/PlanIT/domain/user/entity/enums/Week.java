package com.sideProject.PlanIT.domain.user.entity.enums;

import java.time.DayOfWeek;

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
}
