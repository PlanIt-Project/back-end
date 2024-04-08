package com.sideProject.PlanIT.domain.program.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

@Getter
public class RegistrationRequestDto {
    @NotNull(message = "상품 아이디가 없습니다")
    Long productId;
    @NotNull(message = "등록 날짜가 없습니다")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    LocalDate registrationAt;
    Long trainerId;

    @Builder
    public RegistrationRequestDto(Long productId, Long trainerId, LocalDate registrationAt) {
        this.productId = productId;
        this.trainerId = trainerId;
        this.registrationAt = registrationAt;
    }
}
