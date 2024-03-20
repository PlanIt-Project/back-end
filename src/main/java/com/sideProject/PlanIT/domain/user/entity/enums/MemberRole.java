package com.sideProject.PlanIT.domain.user.entity.enums;

import java.util.List;

public enum MemberRole {
    MEMBER,
    TRAINER,
    ADMIN;

    public static List<MemberRole> MemberAndTrainer() {
        return List.of(MEMBER,TRAINER);
    }
}
