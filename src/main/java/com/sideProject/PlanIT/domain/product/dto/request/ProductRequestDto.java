package com.sideProject.PlanIT.domain.product.dto.request;

import com.sideProject.PlanIT.domain.product.entity.enums.ProductType;
import lombok.AccessLevel;
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
}
