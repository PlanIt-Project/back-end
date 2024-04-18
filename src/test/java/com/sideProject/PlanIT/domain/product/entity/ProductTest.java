package com.sideProject.PlanIT.domain.product.entity;

import com.sideProject.PlanIT.domain.product.entity.enums.ProductSellingType;
import com.sideProject.PlanIT.domain.product.entity.enums.ProductType;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class ProductTest {

    @Nested
    @DisplayName("stopSellingTest")
    public class stopSellingTest {

        @DisplayName("상품의 판매상태를 판매중지로 변경한다")
        @Test
        public void stopSelling() {

            // given
            Product product = Product.builder()
                    .name("test")
                    .number(10)
                    .price(300000)
                    .type(ProductType.PT)
                    .sellingType(ProductSellingType.SELLING)
                    .build();

            // when
            product.stopSelling();

            // then
            assertThat(product.getName()).isEqualTo("test");
            assertThat(product.getSellingType()).isEqualTo(ProductSellingType.STOP_SELLING);
        }
    }
}
