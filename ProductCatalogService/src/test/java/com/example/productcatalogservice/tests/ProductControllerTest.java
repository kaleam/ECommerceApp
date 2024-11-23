package com.example.productcatalogservice.tests;

import com.example.productcatalogservice.controllers.ProductController;
import com.example.productcatalogservice.dtos.ProductDto;
import com.example.productcatalogservice.models.Product;
import com.example.productcatalogservice.services.IProductService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@SpringBootTest
class ProductControllerTest {
    @Autowired
    ProductController productController;

    @MockBean
    IProductService productService;

//    @Test
//    void getProducts() {
//    }

    @Test
    public void Test_getProductById_Success() {
        Product product = new Product();
        product.setId(1L);
        when(productService.getProductById(1L)).thenReturn(product);
        ResponseEntity<ProductDto> response = productController.getProductById(1L);
        assertNotNull(response);
        assertEquals(1L,response.getBody().getId());
    }

    @Test
    public void Test_getProductById_ThrowsIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class, () -> productController.getProductById(-1L));
    }

//    @Test
//    void createProduct() {
//    }
//
//    @Test
//    void replaceProduct() {
//    }
}