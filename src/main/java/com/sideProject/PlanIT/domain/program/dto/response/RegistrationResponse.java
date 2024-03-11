package com.sideProject.PlanIT.domain.program.dto.response;

import com.sideProject.PlanIT.domain.product.dto.ProductDto;
import com.sideProject.PlanIT.domain.product.dto.response.ProductResponseDto;
import com.sideProject.PlanIT.domain.product.entity.Product;
import com.sideProject.PlanIT.domain.program.entity.ENUM.RegistrationStatus;
import com.sideProject.PlanIT.domain.program.entity.Program;
import com.sideProject.PlanIT.domain.program.entity.Registration;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class RegistrationResponse {
    private Long id;
    private String registrationAt;
    private String refundAt;
    private RegistrationStatus status;
    private int discount;
    private int totalPrice;
    private ProductResponseDto product;
    private Long memberId;
    private String member;


    public static RegistrationResponse of(Registration registration){
        //환불 여부 null 체크
        String registrationAt = registration.getRefundAt() != null ?
                registration.getRefundAt().toString() :
                "";

        return RegistrationResponse.builder()
                .id(registration.getId())
                .registrationAt(registration.getRegistrationAt().toString())
                .refundAt(registrationAt)
                .status(registration.getStatus())
                .discount(registration.getDiscount())
                .totalPrice(registration.getTotalPrice())
                .memberId(registration.getMember().getId())
                .member(registration.getMember().getName())
                .product(Product.toDto(registration.getProduct()))
                .build();
    }
}
