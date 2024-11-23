package com.example.productcatalogservice.controllers;

import com.example.productcatalogservice.dtos.CategoryDto;
import com.example.productcatalogservice.dtos.ProductDto;
import com.example.productcatalogservice.models.Category;
import com.example.productcatalogservice.models.Product;
import com.example.productcatalogservice.services.IProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/products")
public class ProductController {

    @Autowired
    private IProductService productService;

    @GetMapping
    public List<ProductDto> getProducts(){
        List<Product> products = productService.getAllProducts();
        List<ProductDto> productDtos = new ArrayList<>();
        for (Product product : products) {
            productDtos.add(getProductDto(product));
        }
        return productDtos;
    }

    @GetMapping("{id}")
    public ResponseEntity<ProductDto> getProductById(@PathVariable Long id){
        if (id <= 0)
            throw new IllegalArgumentException("product id is invalid");
        Product product = productService.getProductById(id);
        ProductDto productDto = getProductDto(product);
        MultiValueMap<String, String> headers = new LinkedMultiValueMap<>();
        headers.add("called by", "Abhijeet");
        return new ResponseEntity<>(productDto, headers, HttpStatus.OK);
    }

    @PostMapping
    public ProductDto createProduct(@RequestBody ProductDto productDto){
        Product product = productService.createProduct(getProduct(productDto));
        return getProductDto(product);
    }

    @PutMapping("{id}")
    public ProductDto replaceProduct(@PathVariable Long id, @RequestBody ProductDto productDto){
        Product input = getProduct(productDto);
        Product output = productService.updateProduct(id, input);
        return getProductDto(output);
    }

    private Product getProduct(ProductDto productDto){
        Product product = new Product();
        product.setId(productDto.getId());
        product.setName(productDto.getName());
        product.setDescription(productDto.getDescription());
        product.setPrice(productDto.getPrice());
        if(productDto.getCategory() != null) {
            Category category = new Category();
            category.setName(productDto.getCategory().getName());
            category.setId(productDto.getCategory().getId());
            product.setCategory(category);
        }
        return product;
    }

    private ProductDto getProductDto(Product product){
        ProductDto productDto = new ProductDto();
        productDto.setId(product.getId());
        productDto.setName(product.getName());
        productDto.setPrice(product.getPrice());
        productDto.setDescription(product.getDescription());
        productDto.setImageUrl(product.getImageUrl());
        if(product.getCategory() != null){
            CategoryDto categoryDto = new CategoryDto();
            categoryDto.setId(product.getCategory().getId());
            categoryDto.setName(product.getCategory().getName());
            productDto.setCategory(categoryDto);
        }
        return productDto;
    }
}
