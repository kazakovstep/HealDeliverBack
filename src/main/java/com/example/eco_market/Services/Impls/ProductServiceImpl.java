package com.example.eco_market.Services.Impls;

import com.example.eco_market.Models.Product;
import com.example.eco_market.Repositories.ProductRepository;
import com.example.eco_market.Services.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;

    @Override
    public List<Product> listProducts(String title) {
        List<Product> products;
        if (title != null) {
            products = productRepository.findByTitleContaining(title);
        } else {
            products = productRepository.findAll();
        }
        return products;
    }

    @Override
    public Product getProductById(Long id) {
        return productRepository.findById(id).orElse(null);
    }

    @Override
    public List<Product> getProductsByCategory(String category) {
        return productRepository.findByCategory(category);
    }

    @Override
    public void saveProduct(Product product) {
        productRepository.save(product);
    }


    @Override
    public void deleteProduct(Long id) {
        productRepository.deleteById(id);
    }

}
