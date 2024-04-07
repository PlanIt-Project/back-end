package com.sideProject.PlanIT.domain.program.dto.request;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import static io.lettuce.core.pubsub.PubSubOutput.Type.message;

@Getter
@NoArgsConstructor
public class ApproveRequestDto {
    @NotNull(message="트레이너 아이디가 없습니다.")
    Long trainer;
    @Builder
    public ApproveRequestDto(Long trainer) {
        this.trainer = trainer;
    }
}
