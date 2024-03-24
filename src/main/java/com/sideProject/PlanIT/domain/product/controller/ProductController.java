package com.sideProject.PlanIT.domain.product.controller;

import com.sideProject.PlanIT.common.response.ApiResponse;
import com.sideProject.PlanIT.domain.product.controller.enums.ProductSearchOption;
import com.sideProject.PlanIT.domain.product.dto.response.ProductResponseDto;
import com.sideProject.PlanIT.domain.product.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/product")
public class ProductController {

    private final ProductService productService;

    @GetMapping
    public ApiResponse<Page<ProductResponseDto>> findAllProducts(
            @RequestParam(value = "option", required = false, defaultValue = "SELLING") ProductSearchOption option,
            @PageableDefault(size = 10,sort = "id",direction = Sort.Direction.DESC) Pageable pageable
    ) {
        return ApiResponse.ok(productService.findAllProducts(option,pageable));
    }

    @GetMapping("/{product_id}")
    public ApiResponse<ProductResponseDto> findProduct(@PathVariable(name = "product_id") Long product_id) {
        return ApiResponse.ok(productService.findProduct(product_id));
    }
}
