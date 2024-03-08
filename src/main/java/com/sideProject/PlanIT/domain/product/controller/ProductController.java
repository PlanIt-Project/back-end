package com.sideProject.PlanIT.domain.product.controller;

import com.sideProject.PlanIT.common.response.ApiResponse;
import com.sideProject.PlanIT.common.response.ErrorCode;
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
    public ApiResponse<?> createProduct(@RequestBody ProductDto.ProductRequestDto productRequestDto) {
        try {
            return ApiResponse.ok(productService.createProduct(productRequestDto));
        } catch (Exception e) {
            return ApiResponse.error(ErrorCode.INVALID_PARAMETER);
        }
    }

    @PutMapping("/product/{product_id}")
    public ApiResponse<?> editProduct(@PathVariable Long product_id, @RequestBody ProductDto.ProductRequestDto productRequestDto) {
        try {
            return ApiResponse.ok(productService.editProduct(product_id, productRequestDto));
        } catch (Exception e) {
            return ApiResponse.error(ErrorCode.INVALID_PARAMETER);
        }
    }

    @DeleteMapping("/product/{product_id}")
    public ApiResponse<?> deleteProduct(@PathVariable Long product_id) {
        try {
            return ApiResponse.ok(productService.deleteProduct(product_id));
        } catch (Exception e) {
            return ApiResponse.error(ErrorCode.INVALID_PARAMETER);
        }
    }

    @GetMapping("/product")
    public ApiResponse<?> findAllProducts() {
        try {
            return ApiResponse.ok(productService.findAllProducts());
        } catch (Exception e) {
            return ApiResponse.error(ErrorCode.RESOURCE_NOT_FOUND);
        }
    }

    @GetMapping("/product/{product_id}")
    public ApiResponse<?> findProduct(@PathVariable Long product_id) {
        try {
            return ApiResponse.ok(productService.findProduct(product_id));
        } catch (Exception e) {
            return ApiResponse.error(ErrorCode.INVALID_PARAMETER);
        }
    }
}
