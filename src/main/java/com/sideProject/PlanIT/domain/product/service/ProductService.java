package com.sideProject.PlanIT.domain.product.service;

import com.sideProject.PlanIT.domain.product.dto.ProductDto;
import com.sideProject.PlanIT.domain.product.entity.Product;
import com.sideProject.PlanIT.domain.product.repository.ProductRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

public interface ProductService {
    Product createProduct(ProductDto.ProductRequestDto productRequestDto);
    Product editProduct(Long product_id, ProductDto.ProductRequestDto productRequestDto);

    String deleteProduct(Long product_id);
    List<ProductDto.ProductResponseDto> findAllProducts();
    ProductDto.ProductResponseDto findProduct(Long product_id);
}
