package com.sideProject.PlanIT.domain.product.dto.request;

import com.sideProject.PlanIT.domain.product.entity.ENUM.ProductType;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.Period;

@Getter
public class ProductRequestDto {
    private String name;
    private Period period;
    private int number;
    private int price;
    private ProductType type;
}
