package com.sideProject.PlanIT.domain.program.entity.enums;

import java.util.List;

public enum ProgramStatus {
    NOT_STARTED,
    IN_PROGRESS,
    SUSPEND,
    REFUND,
    EXPIRED;

    public static List<ProgramStatus> forValid() {
        return List.of(IN_PROGRESS);
    }

    public static List<ProgramStatus> forUnValid() {
        return List.of(NOT_STARTED,SUSPEND,REFUND,EXPIRED);
    }
}
