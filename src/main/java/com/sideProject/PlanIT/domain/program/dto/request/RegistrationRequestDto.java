package com.sideProject.PlanIT.domain.program.dto.request;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Getter
@NoArgsConstructor
public class RegistrationRequestDto {
    Long productId;
    Long trainerId;
    LocalDate registrationAt;

    @Builder
    public RegistrationRequestDto(Long productId, Long trainerId, LocalDate registrationAt) {
        this.productId = productId;
        this.trainerId = trainerId;
        this.registrationAt = registrationAt;
    }
}
