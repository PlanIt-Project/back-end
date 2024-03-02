package com.sideProject.PlanIT.domain.product.entity;

import com.sideProject.PlanIT.domain.product.entity.ENUM.ProductType;
import jakarta.persistence.*;

import java.time.Period;

@Entity
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "product_id")
    private Long Id;

    @Column(name = "product_name")
    private String name;

    @Column
    private Period period;

    @Column
    private int number;

    @Column
    private int price;

    @Enumerated(EnumType.STRING)
    private ProductType type;
}
