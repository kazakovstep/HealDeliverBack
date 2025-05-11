package com.example.eco_market.Repositories;

import com.example.eco_market.Models.Product;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProductRepository extends JpaRepository<Product, Long> {
    List<Product> findByTitleContaining(String title);

    List<Product> findByCategory(String category);
}
