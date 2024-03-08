package com.sideProject.PlanIT.domain.product.service;

import com.sideProject.PlanIT.domain.product.dto.ProductDto;
import com.sideProject.PlanIT.domain.product.entity.Product;
import com.sideProject.PlanIT.domain.product.repository.ProductRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

public interface ProductService {
    Product createProduct(ProductDto.ProductRequestDto productRequestDto);
    Product editProduct(Long product_id, ProductDto.ProductRequestDto productRequestDto);
}
