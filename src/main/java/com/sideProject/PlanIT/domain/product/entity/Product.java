package com.sideProject.PlanIT.domain.product.entity;

import com.sideProject.PlanIT.domain.product.dto.request.ProductRequestDto;
import com.sideProject.PlanIT.domain.product.dto.response.ProductResponseDto;
import com.sideProject.PlanIT.domain.product.entity.enums.ProductSellingType;
import com.sideProject.PlanIT.domain.product.entity.enums.ProductType;
import jakarta.persistence.*;
import lombok.*;

import java.time.Period;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    private String name;

    @Column
    private Period period;

    @Column
    private int number;

    @Column
    private int price;

    @Enumerated(EnumType.STRING)
    private ProductType type;

    @Enumerated(EnumType.STRING)
    private ProductSellingType sellingType;

    @Builder
    public Product(Long id,String name, Period period, int number, int price, ProductType type, ProductSellingType sellingType) {
        this.id = id;
        this.name = name;
        this.period = period;
        this.number = number;
        this.price = price;
        this.type = type;
        this.sellingType = sellingType;
    }

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
                .id(product.id)
                .name(product.name)
                .number(product.number)
                .period(product.period)
                .price(product.price)
                .type(product.type)
                .build();
    }

    public void stopSelling() {
        this.sellingType = ProductSellingType.STOP_SELLING;
    }

    public void reSelling() {
        this.sellingType = ProductSellingType.SELLING;
    }
}
