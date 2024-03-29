package com.sideProject.PlanIT.domain.product.controller;

import com.sideProject.PlanIT.common.response.ApiResponse;
import com.sideProject.PlanIT.domain.product.dto.request.ProductRequestDto;
import com.sideProject.PlanIT.domain.product.entity.Product;
import com.sideProject.PlanIT.domain.product.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/admin/product")
@RequiredArgsConstructor
public class ProductAdminController {
    private final ProductService productService;

    @PostMapping
    public ApiResponse<Product> createProduct(@RequestBody ProductRequestDto productRequestDto) {
        return ApiResponse.ok(productService.createProduct(productRequestDto));
    }

    @DeleteMapping("/{product_id}")
    public ApiResponse<String> deleteProduct(@PathVariable(name = "product_id") Long product_id) {
        return ApiResponse.ok(productService.deleteProduct(product_id));
    }
}
