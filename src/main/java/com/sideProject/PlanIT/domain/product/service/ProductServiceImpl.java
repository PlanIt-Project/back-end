package com.sideProject.PlanIT.domain.product.service;

import com.sideProject.PlanIT.domain.product.dto.ProductDto;
import com.sideProject.PlanIT.domain.product.dto.request.ProductRequestDto;
import com.sideProject.PlanIT.domain.product.dto.response.ProductResponseDto;
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
                .build());
    }

    @Override
    public Product editProduct(Long product_id, ProductRequestDto productRequestDto) {
        Product productToEdit = productRepository.findById(product_id).orElseThrow(() -> new IllegalArgumentException("no exist id"));
        return productRepository.save(productToEdit.update(productRequestDto));
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
                .map(Product::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public ProductResponseDto findProduct(Long product_id) {
        return Product.toDto(productRepository.findById(product_id).orElseThrow(() -> new IllegalArgumentException("no exist id")));
    }
}
