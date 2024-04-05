package com.sideProject.PlanIT.domain.program.dto.request;
import com.sideProject.PlanIT.domain.product.entity.Product;
import com.sideProject.PlanIT.domain.user.entity.Member;
import com.sideProject.PlanIT.domain.program.entity.enums.RegistrationStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

public class ProgramRegistraionDto {
    @Getter
    @AllArgsConstructor
    public static class programRegistrationrequest {

        private LocalDateTime registrationAt;
        private LocalDateTime paymentAt;
        private LocalDateTime refundAt;
        private RegistrationStatus status;
        private int discount;
        private int totalPrice;
        private Member member;
        private Product product;

    }
}
