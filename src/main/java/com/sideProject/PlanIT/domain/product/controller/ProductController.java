package com.sideProject.PlanIT.domain.product.controller;

import com.sideProject.PlanIT.common.response.ApiResponse;
import com.sideProject.PlanIT.domain.product.dto.ProductDto;
import com.sideProject.PlanIT.domain.product.entity.Product;
import com.sideProject.PlanIT.domain.product.service.ProductService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@AllArgsConstructor
public class ProductController {

    private final ProductService productService;

    @PostMapping("/product")
    public ApiResponse<Product> createProduct(@RequestBody ProductDto.ProductRequestDto productRequestDto) {
        System.out.println(productRequestDto.getName());
        System.out.println(productRequestDto.getPrice());
        System.out.println(productRequestDto.getNumber());
        System.out.println(productRequestDto.getType());
        System.out.println(productRequestDto.getPeriod());
        return ApiResponse.ok(productService.createProduct(productRequestDto));
    }

    @PutMapping("/product/{product_id}")
    public ApiResponse<Product> editProduct(@PathVariable Long product_id, @RequestBody ProductDto.ProductRequestDto productRequestDto) {
        return ApiResponse.ok(productService.editProduct(product_id, productRequestDto));
    }
}
