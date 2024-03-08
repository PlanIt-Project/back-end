package com.sideProject.PlanIT.domain.product.service;

import com.sideProject.PlanIT.domain.product.dto.ProductDto;
import com.sideProject.PlanIT.domain.product.entity.Product;
import com.sideProject.PlanIT.domain.product.repository.ProductRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class ProductServiceImpl implements ProductService{

    private final ProductRepository productRepository;

    @Override
    public Product createProduct(ProductDto.ProductRequestDto productRequestDto) {
        return productRepository.save(Product.builder()
                        .name(productRequestDto.getName())
                        .period(productRequestDto.getPeriod())
                        .number(productRequestDto.getNumber())
                        .price(productRequestDto.getPrice())
                        .type(productRequestDto.getType())
                .build());
    }

    @Override
    public Product editProduct(Long product_id, ProductDto.ProductRequestDto productRequestDto) {
        Product productToEdit = productRepository.findById(product_id).orElseThrow(() -> new IllegalArgumentException("no exist id"));
        return productRepository.save(productToEdit.update(productRequestDto));
    }
}
