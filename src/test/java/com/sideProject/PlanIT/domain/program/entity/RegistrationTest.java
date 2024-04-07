package com.sideProject.PlanIT.domain.program.entity;

import com.sideProject.PlanIT.domain.program.entity.enums.RegistrationStatus;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.*;

class RegistrationTest {
    @DisplayName("등록을 승인할 수 있다.")
    @Test
    void changeToAccepted(){
        //given
        Registration registration = Registration.builder()
                .status(RegistrationStatus.PENDING)
                .build();
        //when
        registration.changeToAccepted();
        //then
        assertThat(registration.getStatus()).isEqualTo(RegistrationStatus.ACCEPTED);
    }

    @DisplayName("등록을 취소할 수 있다.")
    @Test
    void changeToDECLINED(){
        //given
        Registration registration = Registration.builder()
                .status(RegistrationStatus.PENDING)
                .build();
        //when
        registration.changeToDeclined();
        //then
        assertThat(registration.getStatus()).isEqualTo(RegistrationStatus.DECLINED);
    }

    @DisplayName("등록을 환불할 수 있다.")
    @Test
    void changeToREFUND(){
        //given
        Registration registration = Registration.builder()
                .status(RegistrationStatus.ACCEPTED)
                .build();
        LocalDateTime now = LocalDateTime.now();
        //when
        registration.changeToRefund(now);
        //then
        assertThat(registration.getStatus()).isEqualTo(RegistrationStatus.REFUND);
    }
}