package com.sideProject.PlanIT.domain.product.repository;

import com.sideProject.PlanIT.domain.product.entity.enums.ProductSellingType;
import com.sideProject.PlanIT.domain.product.entity.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
    Page<Product> findAllBySellingType(ProductSellingType sellingTypes, Pageable pageable);
    Optional<Product> findByName(String name);
}
