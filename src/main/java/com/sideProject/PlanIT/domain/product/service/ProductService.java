package com.sideProject.PlanIT.domain.product.service;

import com.sideProject.PlanIT.domain.product.dto.request.ProductRequestDto;
import com.sideProject.PlanIT.domain.product.dto.response.ProductResponseDto;
import com.sideProject.PlanIT.domain.product.entity.Product;

import java.util.List;

public interface ProductService {
    Product createProduct(ProductRequestDto productRequestDto);
    Product editProduct(Long product_id, ProductRequestDto productRequestDto);

    String deleteProduct(Long product_id);
    List<ProductResponseDto> findAllProducts();
    ProductResponseDto findProduct(Long product_id);
}
