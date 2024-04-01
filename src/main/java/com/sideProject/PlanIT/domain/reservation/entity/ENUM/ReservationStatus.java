package com.sideProject.PlanIT.domain.reservation.entity.ENUM;

import com.sideProject.PlanIT.domain.reservation.controller.ENUM.ReservationFindOption;

import java.util.List;

public enum ReservationStatus {
    RESERVED,
    POSSIBLE,
    FINISHED;

    public static List<ReservationStatus> forReserved() {
        return List.of(RESERVED);
    }
    public static List<ReservationStatus> forPossible() {
        return List.of(POSSIBLE);
    }
    public static List<ReservationStatus> forFinished() {
        return List.of(FINISHED);
    }
}
