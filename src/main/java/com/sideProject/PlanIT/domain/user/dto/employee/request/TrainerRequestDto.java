package com.sideProject.PlanIT.domain.user.dto.employee.request;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class TrainerRequestDto {
    private String career;
}
