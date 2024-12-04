package com.example.productcatalogservice.services;

import com.example.productcatalogservice.models.Product;
import org.springframework.data.domain.Page;

import java.util.List;

public interface IProductService {
    public Product getProductById(Long id);

    public Page<Product> getAllProducts(int page, int size);

    public Product createProduct(Product product);

    public Product updateProduct(Long id, Product product);
}
