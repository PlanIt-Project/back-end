package com.sideProject.PlanIT.domain.product.dto.request;

import com.sideProject.PlanIT.domain.product.entity.enums.ProductSellingType;
import com.sideProject.PlanIT.domain.product.entity.enums.ProductType;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.Period;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ProductRequestDto {
    private String name;
    private Period period;
    private int number;
    private int price;
    private ProductType type;
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
