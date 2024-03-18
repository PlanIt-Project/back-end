package com.sideProject.PlanIT.domain.product.service;

import com.sideProject.PlanIT.domain.product.dto.request.ProductRequestDto;
import com.sideProject.PlanIT.domain.product.dto.response.ProductResponseDto;
import com.sideProject.PlanIT.domain.product.entity.ENUM.ProductStatus;
import com.sideProject.PlanIT.domain.product.entity.Product;
import com.sideProject.PlanIT.domain.product.repository.ProductRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class ProductServiceImpl implements ProductService{

    private final ProductRepository productRepository;

    @Override
    public Product createProduct(ProductRequestDto productRequestDto) {
        return productRepository.save(Product.builder()
                        .name(productRequestDto.getName())
                        .period(productRequestDto.getPeriod())
                        .number(productRequestDto.getNumber())
                        .price(productRequestDto.getPrice())
                        .type(productRequestDto.getType())
                        .status(ProductStatus.NOT_SELLING)
                .build());
    }

    @Override
    public String deleteProduct(Long product_id) {
        productRepository.deleteById(product_id);
        return "삭제 성공";
    }

    @Override
    public List<ProductResponseDto> findAllProducts() {
        List<Product> products = productRepository.findAll();
        return products.stream()
                .map(ProductResponseDto::of)
                .collect(Collectors.toList());
    }

    @Override
    public ProductResponseDto findProduct(Long product_id) {
        return ProductResponseDto.of(productRepository.findById(product_id).orElseThrow(() -> new IllegalArgumentException("no exist id")));
    }
}
