package com.sideProject.PlanIT.domain.program.dto.response;

import com.sideProject.PlanIT.domain.product.dto.response.ProductResponseDto;
import com.sideProject.PlanIT.domain.program.entity.Registration;
import com.sideProject.PlanIT.domain.program.entity.enums.RegistrationStatus;
import com.sideProject.PlanIT.domain.user.dto.member.response.MemberSemiResponseDto;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class FindRegistrationResponse {
    private Long id;
    private String registrationAt;
    private String refundAt;
    private RegistrationStatus status;
    private int discount;
    private int totalPrice;
    private ProductResponseDto product;
    private MemberSemiResponseDto member;
    private Long trainerId;

    @Builder
    public FindRegistrationResponse(Long id, String registrationAt, String refundAt, RegistrationStatus status, int discount, int totalPrice, ProductResponseDto product, MemberSemiResponseDto member, Long trainerId) {
        this.id = id;
        this.registrationAt = registrationAt;
        this.refundAt = refundAt;
        this.status = status;
        this.discount = discount;
        this.totalPrice = totalPrice;
        this.product = product;
        this.member = member;
        this.trainerId = trainerId;
    }

    public static FindRegistrationResponse of(Registration registration){
        //환불 여부 null 체크
        String registrationAt = registration.getRefundAt() != null ?
                registration.getRefundAt().toString() :
                "";


        return FindRegistrationResponse.builder()
                .id(registration.getId())
                .registrationAt(registration.getRegistrationAt().toString())
                .refundAt(registrationAt)
                .status(registration.getStatus())
                .discount(registration.getDiscount())
                .totalPrice(registration.getTotalPrice())
                .product(ProductResponseDto.of(registration.getProduct()))
                .member(MemberSemiResponseDto.of(registration.getMember()))
                .trainerId(registration.getTrainerId())
                .build();
    }
}
