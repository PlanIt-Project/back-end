package com.sideProject.PlanIT.domain.product.dto.response;

import com.sideProject.PlanIT.domain.product.entity.ENUM.ProductType;
import com.sideProject.PlanIT.domain.product.entity.Product;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.Period;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
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

    public static ProductResponseDto of(Product product) {
        return ProductResponseDto.builder()
                .id(product.getId())
                .name(product.getName())
                .period(product.getPeriod())
                .number(product.getNumber())
                .price(product.getPrice())
                .type(product.getType())
                .build();
    }
}
