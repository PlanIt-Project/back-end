package com.sideProject.PlanIT.domain.product.dto.request;

import com.sideProject.PlanIT.domain.product.entity.ENUM.ProductType;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ProductRequestDto {
    private String name;
    private String period;
    private int number;
    private int price;
    private ProductType type;
}
