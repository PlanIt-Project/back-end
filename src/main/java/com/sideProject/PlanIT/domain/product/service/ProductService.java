package com.sideProject.PlanIT.domain.product.service;

import com.sideProject.PlanIT.domain.product.controller.enums.ProductSearchOption;
import com.sideProject.PlanIT.domain.product.dto.request.ProductRequestDto;
import com.sideProject.PlanIT.domain.product.dto.response.ProductResponseDto;
import com.sideProject.PlanIT.domain.product.entity.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ProductService {
    Product createProduct(ProductRequestDto productRequestDto);
    String stopProductSell(Long product_id);
    Page<ProductResponseDto> findAllProducts(ProductSearchOption option, Pageable pageable);
    ProductResponseDto findProduct(Long product_id);
}
