package com.sideProject.PlanIT.domain.product.dto.response;

import com.sideProject.PlanIT.domain.product.entity.enums.ProductSellingType;
import com.sideProject.PlanIT.domain.product.entity.enums.ProductType;
import com.sideProject.PlanIT.domain.product.entity.Product;
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
    private ProductSellingType sellingType;

    @Builder
    public ProductResponseDto(Long id,String name, Period period, int number, int price, ProductType type,ProductSellingType sellingType) {
        this.id = id;
        this.name = name;
        this.period = period;
        this.number = number;
        this.price = price;
        this.type = type;
        this.sellingType = sellingType;
    }

    public static ProductResponseDto of(Product product) {
        return ProductResponseDto.builder()
                .id(product.getId())
                .name(product.getName())
                .period(product.getPeriod())
                .number(product.getNumber())
                .price(product.getPrice())
                .type(product.getType())
                .sellingType(product.getSellingType())
                .build();
    }
}
