package com.sideProject.PlanIT.domain.product.controller;

import com.sideProject.PlanIT.common.response.ApiResponse;
import com.sideProject.PlanIT.common.response.ErrorCode;
import com.sideProject.PlanIT.domain.product.dto.ProductDto;
import com.sideProject.PlanIT.domain.product.dto.request.ProductRequestDto;
import com.sideProject.PlanIT.domain.product.dto.response.ProductResponseDto;
import com.sideProject.PlanIT.domain.product.entity.Product;
import com.sideProject.PlanIT.domain.product.service.ProductService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@AllArgsConstructor
public class ProductController {

    private final ProductService productService;

    @PostMapping("/product")
    public ApiResponse<Product> createProduct(@RequestBody ProductRequestDto productRequestDto) {
        return ApiResponse.ok(productService.createProduct(productRequestDto));
    }

    @PutMapping("/product/{product_id}")
    public ApiResponse<Product> editProduct(@PathVariable Long product_id, @RequestBody ProductRequestDto productRequestDto) {
        return ApiResponse.ok(productService.editProduct(product_id, productRequestDto));
    }

    @DeleteMapping("/product/{product_id}")
    public ApiResponse<String> deleteProduct(@PathVariable Long product_id) {
        return ApiResponse.ok(productService.deleteProduct(product_id));
    }

    @GetMapping("/product")
    public ApiResponse<List<ProductResponseDto>> findAllProducts() {
        return ApiResponse.ok(productService.findAllProducts());
    }

    @GetMapping("/product/{product_id}")
    public ApiResponse<ProductResponseDto> findProduct(@PathVariable Long product_id) {
        return ApiResponse.ok(productService.findProduct(product_id));
    }
}
