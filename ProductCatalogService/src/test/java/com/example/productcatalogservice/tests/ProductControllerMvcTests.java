package com.example.productcatalogservice.tests;

import com.example.productcatalogservice.controllers.ProductController;
import com.example.productcatalogservice.dtos.CategoryDto;
import com.example.productcatalogservice.dtos.ProductDto;
import com.example.productcatalogservice.models.Category;
import com.example.productcatalogservice.models.Product;
import com.example.productcatalogservice.services.IProductService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultMatcher;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ProductController.class)
public class ProductControllerMvcTests {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private IProductService productService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    public void Test_GetAllProducts_CheckStatusOnly() throws Exception {
        mockMvc.perform(get("/products"))
                .andExpect(status().is2xxSuccessful());
    }

    @Test
    public void Test_GetProduct_CheckStatusAndResponseContent() throws Exception {
        Product product1 = new Product();
        product1.setName("iphone15");
        Product product2 = new Product();
        product2.setName("iphone16");
        List<Product> productList = new ArrayList<>();
        productList.add(product1);
        productList.add(product2);
        Page<Product> productPage = new PageImpl<>(productList, PageRequest.of(0,10), productList.size());

        when(productService.getAllProducts(0,10)).thenReturn(productPage);

        mockMvc.perform(get("/products"))
                .andExpect(status().is2xxSuccessful())
                .andExpect(content().string(objectMapper.writeValueAsString(productList)));
    }

    @Test
    public void Test_CreateProduct_CheckStatus() throws Exception {
        ProductDto productDto = new ProductDto();
        productDto.setId(1L);
        productDto.setName("iphone15");
        productDto.setDescription("iphone15");
        CategoryDto categoryDto = new CategoryDto();
        categoryDto.setName("phones");
        productDto.setCategory(categoryDto);

        Product product = new Product();
        product.setId(1L);
        product.setName("iphone15");
        product.setDescription("iphone15");
        Category category = new Category();
        category.setName("phones");
        product.setCategory(category);

        when(productService.createProduct(any(Product.class))).thenReturn(product);

        mockMvc.perform(post("/products")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(productDto)))
                .andExpect(status().is2xxSuccessful())
                .andExpect(content().string(objectMapper.writeValueAsString(productDto)))
                .andExpect(jsonPath("$.name").value("iphone15"));
    }
}
