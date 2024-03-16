package com.sideProject.PlanIT.domain.program.dto.response;

import com.sideProject.PlanIT.domain.program.entity.Registration;
import lombok.Builder;
import lombok.Getter;

@Getter
public class RegistrationResponse {
    Long id;
    String message;

    @Builder
    public RegistrationResponse(Long id, String message) {
        this.id = id;
        this.message = message;
    }

    public static RegistrationResponse of(Long id, String message) {
        return RegistrationResponse.builder()
                .id(id)
                .message(message)
                .build();
    }
}
