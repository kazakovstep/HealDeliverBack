package com.example.eco_market.Services;

import com.example.eco_market.Models.Product;
import com.example.eco_market.Repositories.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public interface ProductService {

    List<Product> listProducts(String title);

    Product getProductById(Long id);

    List<Product> getProductsByCategory(String category);

    void saveProduct(Product product);

    void deleteProduct(Long id);

}
