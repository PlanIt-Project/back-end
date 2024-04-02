package com.sideProject.PlanIT.domain.product.service;

import com.sideProject.PlanIT.common.response.CustomException;
import com.sideProject.PlanIT.domain.product.controller.enums.ProductSearchOption;
import com.sideProject.PlanIT.domain.product.dto.request.ProductRequestDto;
import com.sideProject.PlanIT.domain.product.dto.response.ProductResponseDto;
import com.sideProject.PlanIT.domain.product.entity.Product;
import com.sideProject.PlanIT.domain.product.entity.enums.ProductSellingType;
import com.sideProject.PlanIT.domain.product.entity.enums.ProductType;
import com.sideProject.PlanIT.domain.product.repository.ProductRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.ActiveProfiles;

import java.time.Period;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest
@ActiveProfiles("test")
public class ProductServiceTest {

    @Autowired
    ProductRepository productRepository;

    @Autowired
    ProductService productService;

    @AfterEach
    void tearDown() {
        productRepository.deleteAllInBatch();
    }

    Product initProduct(String name, ProductType type, ProductSellingType sellingType) {
        return productRepository.save(Product.builder()
                .name(name)
                .type(type)
                .sellingType(sellingType)
                .build());
    }

    @Nested
    @DisplayName("createProductTest")
    class createProductTest {

        @DisplayName("새로운 상품을 생성한다")
        @Test
        void createProduct() {

            ProductRequestDto productRequestDto1 = ProductRequestDto.builder()
                    .name("test1")
                    .period(Period.of(1, 2, 3))
                    .number(0)
                    .price(100000)
                    .type(ProductType.MEMBERSHIP)
                    .build();

            ProductRequestDto productRequestDto2 = ProductRequestDto.builder()
                    .name("test2")
                    .period(Period.of(4, 5, 6))
                    .number(10)
                    .price(200000)
                    .type(ProductType.PT)
                    .build();

            // when
            productService.createProduct(productRequestDto1);
            productService.createProduct(productRequestDto2);

            // then
            Product product1 = productRepository.findByName("test1").get();
            Product product2 = productRepository.findByName("test2").get();

            assertThat(product1.getName()).isEqualTo("test1");
            assertThat(product1.getType()).isEqualTo(ProductType.MEMBERSHIP);
            assertThat(product1.getSellingType()).isEqualTo(ProductSellingType.SELLING);
            assertThat(product1.getPrice()).isEqualTo(100000);

            assertThat(product2.getName()).isEqualTo("test2");
            assertThat(product2.getType()).isEqualTo(ProductType.PT);
            assertThat(product2.getSellingType()).isEqualTo(ProductSellingType.SELLING);
            assertThat(product2.getPrice()).isEqualTo(200000);
        }
    }

    @Nested
    @DisplayName("deleteProductTest")
    class stopProductSellingTest {

        @DisplayName("상품을 판매 중지한다.")
        @Test
        void stopProductSelling() {

            // given
            initProduct("test1", ProductType.MEMBERSHIP, ProductSellingType.SELLING);

            // when
            productService.stopProductSell(productRepository.findByName("test1").get().getId());

            // then
            assertThat(productRepository.findByName("test1").get().getSellingType()).isEqualTo(ProductSellingType.STOP_SELLING);
        }
    }

    @Nested
    @DisplayName("findAllProductsTest")
    class findAllProductsTest {

        @DisplayName("모든 상품을 조회한다.")
        void findAllProduct() {

            // given
            initProduct("test1", ProductType.MEMBERSHIP, ProductSellingType.SELLING);
            initProduct("test2", ProductType.MEMBERSHIP, ProductSellingType.SELLING);
            initProduct("test3", ProductType.PT, ProductSellingType.STOP_SELLING);

            // when
            Pageable pageable = PageRequest.of(0, 3);
            Page<ProductResponseDto> result = productService.findAllProducts(ProductSearchOption.ALL, pageable);

            // then
            assertThat(result.getTotalElements()).isEqualTo(3);
            assertThat(result.getTotalPages()).isEqualTo(1);
            assertThat(result.getContent().size()).isEqualTo(3);

            assertThat(result.get().toList().get(0).getName()).isEqualTo("test1");
            assertThat(result.get().toList().get(1).getName()).isEqualTo("test2");
            assertThat(result.get().toList().get(2).getName()).isEqualTo("test3");
        }

        @DisplayName("판매 중인 상품을 전부 조회한다")
        @Test
        void findAllProducts2() {
            // given
            initProduct("test1", ProductType.MEMBERSHIP, ProductSellingType.SELLING);
            initProduct("test2", ProductType.MEMBERSHIP, ProductSellingType.SELLING);
            initProduct("test3", ProductType.PT, ProductSellingType.STOP_SELLING);

            // when
            Pageable pageable = PageRequest.of(0, 3);
            Page<ProductResponseDto> result = productService.findAllProducts(ProductSearchOption.SELLING, pageable);

            // then
            assertThat(result.getTotalElements()).isEqualTo(2);
            assertThat(result.getTotalPages()).isEqualTo(1);
            assertThat(result.getContent().size()).isEqualTo(2);

            assertThat(result.get().toList().get(0).getName()).isEqualTo("test1");
            assertThat(result.get().toList().get(1).getName()).isEqualTo("test2");
        }

        @DisplayName("판매 중인 상품을 전부 조회한다")
        @Test
        void findAllProducts3() {
            // given
            initProduct("test1", ProductType.MEMBERSHIP, ProductSellingType.SELLING);
            initProduct("test2", ProductType.MEMBERSHIP, ProductSellingType.SELLING);
            initProduct("test3", ProductType.PT, ProductSellingType.STOP_SELLING);

            // when
            Pageable pageable = PageRequest.of(0, 3);
            Page<ProductResponseDto> result = productService.findAllProducts(ProductSearchOption.STOP_SELLING, pageable);

            // then
            assertThat(result.getTotalElements()).isEqualTo(1);
            assertThat(result.getTotalPages()).isEqualTo(1);
            assertThat(result.getContent().size()).isEqualTo(1);

            assertThat(result.get().toList().get(0).getName()).isEqualTo("test3");
        }

        @DisplayName("상품이 없으면 오류가 발생한다")
        @Test
        void findAllProducts4() {
            // given, when
            Pageable pageable = PageRequest.of(0, 3);

            // then
            assertThatThrownBy(() -> productService.findAllProducts(ProductSearchOption.ALL, pageable))
                    .isInstanceOf(CustomException.class);

        }
    }

    @Nested
    @DisplayName("findProductTest")
    class findProductTest {

        @DisplayName("상품을 조회한다")
        @Test
        void findProductTest() {
            // given
            initProduct("test1", ProductType.MEMBERSHIP, ProductSellingType.SELLING);

            // when
            ProductResponseDto productResponseDto = productService.findProduct(productRepository.findByName("test1").get().getId());

            // then
            assertThat(productResponseDto.getName()).isEqualTo("test1");
            assertThat(productResponseDto.getType()).isEqualTo(ProductType.MEMBERSHIP);
            assertThat(productResponseDto.getSellingType()).isEqualTo(ProductSellingType.SELLING);
        }

        @DisplayName("조회할 상품이 없으면 오류가 발생한다")
        @Test
        void findProductTest2() {
            // given
            Long productId = 1L;

            // when, then
            assertThatThrownBy(() -> productService.findProduct(productId))
                    .isInstanceOf(CustomException.class)
                    .hasMessage("상품이 존재하지 않습니다.");
        }
    }
}
