package com.sideProject.PlanIT.domain.product.dto;

import com.sideProject.PlanIT.domain.product.entity.ENUM.ProductType;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.Period;

public class ProductDto {
    @Getter
    @AllArgsConstructor
    public static class ProductRequestDto {
        private String name;
        private String period;
        private int number;
        private int price;
        private ProductType type;
    }
}
