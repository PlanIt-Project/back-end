package com.sideProject.PlanIT.domain.product.dto.response;

import com.sideProject.PlanIT.domain.product.entity.ENUM.ProductType;
import lombok.Builder;
import lombok.Getter;

import java.time.Period;

@Getter
public class ProductResponseDto {
    private Long id;
    private String name;
    private Period period;
    private int number;
    private int price;
    private ProductType type;

    @Builder
    public ProductResponseDto(Long id,String name, Period period, int number, int price, ProductType type) {
        this.id = id;
        this.name = name;
        this.period = period;
        this.number = number;
        this.price = price;
        this.type = type;
    }
}
