package com.sideProject.PlanIT.domain.program.dto.request;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Builder
public class RegistrationRequest {
    Long productId;
    Long trainerId;
    private LocalDate registrationAt;
}
