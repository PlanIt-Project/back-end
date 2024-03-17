package com.sideProject.PlanIT.domain.product.dto.request;

import com.sideProject.PlanIT.domain.product.entity.ENUM.ProductType;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ProductRequestDto {
    private String name;
    private String period;
    private int number;
    private int price;
    private ProductType type;
}
