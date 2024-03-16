package com.sideProject.PlanIT.domain.program.dto.request;

import lombok.Getter;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
public class RegistrationRequest {
    Long productId;
    private LocalDate registrationAt;
}
