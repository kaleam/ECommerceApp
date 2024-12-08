package com.example.productcatalogservice;

import com.example.productcatalogservice.controllers.ProductController;
import com.example.productcatalogservice.dtos.CategoryDto;
import com.example.productcatalogservice.dtos.ProductDto;
import com.example.productcatalogservice.models.Category;
import com.example.productcatalogservice.models.Product;
import com.example.productcatalogservice.services.IProductService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
class ProductCatalogServiceApplicationTests {

    @MockBean
    private IProductService productService;

    @Autowired
    private ProductController productController;

    @Test
    public void Test_GetProductById_WithValidId() {
        // Arrange
        Long id = 1L;
        Product product = new Product();
        product.setId(id);
        product.setName("iphone 15");
        product.setPrice(50000.0);
        Category category = new Category();
        category.setName("Phone");
        product.setCategory(category);
        when(productService.getProductById(id)).thenReturn(product);

        // Act
        ResponseEntity<ProductDto> response = productController.getProductById(id);

        // Assert
        assertNotNull(response);
        assertEquals(response.getBody().getName(), "iphone 15");
        assertEquals(response.getBody().getPrice(), 50000.0);
        assertEquals(response.getBody().getCategory().getName(), "Phone");

        verify(productService, times(1)).getProductById(id);
    }

    @Test
    public void Test_GetProductById_WithInvalidId() {
        Long id = -1L;
        Exception exception = assertThrows(IllegalArgumentException.class, () -> productController.getProductById(id));
        assertNotNull(exception);
        assertEquals(exception.getMessage(), "product id is invalid");

        verify(productService, times(0)).getProductById(id);
    }

    @Test
    public void Test_GetProductById_ProductServiceThrowsException(){
        Long id = 2L;
        when(productService.getProductById(id)).thenThrow(new RuntimeException("something went wrong"));

        Exception exception = assertThrows(RuntimeException.class, () -> productController.getProductById(id));
        assertNotNull(exception);
        assertEquals(exception.getMessage(), "something went wrong");
    }

    @Test
    public void Test_CreateProduct(){
        Product product = new Product();
        product.setId(1L);
        product.setName("iphone 15");
        product.setPrice(50000.0);
        Category category = new Category();
        category.setName("Phone");
        product.setCategory(category);
        when(productService.createProduct(any(Product.class))).thenReturn(product);

        ProductDto productDto = new ProductDto();
        productDto.setId(1L);
        productDto.setName("iphone 15");
        productDto.setPrice(50000.0);
        CategoryDto categoryDto = new CategoryDto();
        categoryDto.setName("Phone");
        productDto.setCategory(categoryDto);

        ProductDto createProductResponse = productController.createProduct(productDto);

        assertNotNull(createProductResponse);
        assertEquals(createProductResponse.getName(), "iphone 15");
        assertEquals(createProductResponse.getPrice(), 50000.0);
        assertEquals(createProductResponse.getCategory().getName(), "Phone");

        //verify(productService, times(1)).createProduct(product);
    }
}
