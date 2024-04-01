package com.sideProject.PlanIT.domain.user.dto.employee.response;

import lombok.Builder;
import lombok.Getter;

@Getter
public class TrainerScheduleRegistrationResponse {
    Long trainer_id ;

    String message;

    @Builder
    public TrainerScheduleRegistrationResponse(Long trainer_id, String message){
        this.trainer_id = trainer_id;
        this.message = message;

    }

    public static TrainerScheduleRegistrationResponse of(Long trainer_id, String message){
        return TrainerScheduleRegistrationResponse.builder().trainer_id(trainer_id).message(message).build();
    }


}
