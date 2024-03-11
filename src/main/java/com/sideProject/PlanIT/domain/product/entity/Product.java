package com.sideProject.PlanIT.domain.product.entity;

import com.sideProject.PlanIT.domain.product.dto.ProductDto;
import com.sideProject.PlanIT.domain.product.dto.request.ProductRequestDto;
import com.sideProject.PlanIT.domain.product.dto.response.ProductResponseDto;
import com.sideProject.PlanIT.domain.product.entity.ENUM.ProductType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.Period;

@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long Id;

    private String name;

    @Column
    private String period;

    @Column
    private int number;

    @Column
    private int price;

    @Enumerated(EnumType.STRING)
    private ProductType type;

    public Product update(ProductRequestDto productRequestDto) {
        this.name = productRequestDto.getName();
        this.period = productRequestDto.getPeriod();
        this.number = productRequestDto.getNumber();
        this.price = productRequestDto.getPrice();
        this.type = productRequestDto.getType();

        return this;
    }

    public static ProductResponseDto toDto(Product product) {
        return ProductResponseDto.builder()
                .name(product.name)
                .number(product.number)
                .period(product.period)
                .price(product.price)
                .type(product.type)
                .build();
    }
}
