package com.anem.comboshop.repo;

import com.anem.comboshop.domain.Product;
import com.anem.comboshop.domain.Purpose;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProductRepository extends JpaRepository<Product, Long> {
    List<Product> findByPurposeAndStockGreaterThan(Purpose purpose, int stock);
    List<Product> findByStockGreaterThan(int stock);
    List<Product> findByPurpose(Purpose purpose);
    List<Product> findByNameContainingIgnoreCase(String q);
}
