package com.sideProject.PlanIT.domain.program.dto.request;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
public class RegistrationRequest {
    Long productId;
    Long trainerId;
    LocalDate registrationAt;

    @Builder
    public RegistrationRequest(Long productId, Long trainerId, LocalDate registrationAt) {
        this.productId = productId;
        this.trainerId = trainerId;
        this.registrationAt = registrationAt;
    }
}
