package com.sideProject.PlanIT.domain.program.dto.response;

import lombok.Builder;
import lombok.Getter;

@Getter
public class RegistrationResponseDto {
    Long id;
    String message;

    @Builder
    public RegistrationResponseDto(Long id, String message) {
        this.id = id;
        this.message = message;
    }

    public static RegistrationResponseDto of(Long id, String message) {
        return RegistrationResponseDto.builder()
                .id(id)
                .message(message)
                .build();
    }
}
