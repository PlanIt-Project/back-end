package com.sideProject.PlanIT.domain.product.dto.request;

import com.sideProject.PlanIT.domain.product.entity.enums.ProductSellingType;
import com.sideProject.PlanIT.domain.product.entity.enums.ProductType;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.Period;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ProductRequestDto {
    @NotNull(message = "상품명이 없습니다.")
    private String name;
    @NotNull(message = "상품의 기간이 없습니다.")
    private Period period;
    @NotNull(message = "횟수가 없습니다.")
    private int number;
    @NotNull(message = "금액이 없습니다.")
    private int price;
    @NotNull(message = "상품 타입이 없습니다.")
    private ProductType type;
    @NotNull(message = "판매 상태가 없습니다.")
    private ProductSellingType sellingType;

    @Builder
    public ProductRequestDto(String name, Period period, int number, int price, ProductType type, ProductSellingType sellingType) {
        this.name = name;
        this.period = period;
        this.number = number;
        this.price = price;
        this.type = type;
        this.sellingType = sellingType;
    }
}
