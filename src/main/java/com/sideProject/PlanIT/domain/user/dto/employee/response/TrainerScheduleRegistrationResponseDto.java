package com.sideProject.PlanIT.domain.user.dto.employee.response;

import lombok.Builder;
import lombok.Getter;

@Getter
public class TrainerScheduleRegistrationResponseDto {
    Long trainer_id ;

    String message;

    @Builder
    public TrainerScheduleRegistrationResponseDto(Long trainer_id, String message){
        this.trainer_id = trainer_id;
        this.message = message;

    }

    public static TrainerScheduleRegistrationResponseDto of(Long trainer_id, String message){
        return TrainerScheduleRegistrationResponseDto.builder().trainer_id(trainer_id).message(message).build();
    }


}
