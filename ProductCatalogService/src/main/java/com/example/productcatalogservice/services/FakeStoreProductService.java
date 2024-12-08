package com.example.productcatalogservice.services;

import com.example.productcatalogservice.clients.FakeStoreClient;
import com.example.productcatalogservice.dtos.FakeStoreProductDto;
import com.example.productcatalogservice.models.Category;
import com.example.productcatalogservice.models.Product;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.List;

@Service
@Primary
public class FakeStoreProductService implements IProductService{

    @Autowired
    private FakeStoreClient fakeStoreClient;

    @Override
    public Product getProductById(Long id) {
        FakeStoreProductDto fakeStoreProductDto = fakeStoreClient.getProductById(id);
        if(fakeStoreProductDto != null)
            return getProduct(fakeStoreProductDto);
        return null;
    }

    @Override
    public Page<Product> getAllProducts(int page, int size) {
        return null;
    }

    //@Override
//    public List<Product> getAllProducts() {
//        List<FakeStoreProductDto> fakeStoreProductDtoList = fakeStoreClient.getAllProducts();
//        List<Product> products = new ArrayList<>();
//        for (FakeStoreProductDto fakeStoreProductDto : fakeStoreProductDtoList)
//            products.add(getProduct(fakeStoreProductDto));
//        return products;
//    }

    @Override
    public Product createProduct(Product product) {
        FakeStoreProductDto fakeStoreProductDto = fakeStoreClient.createProduct(getFakeStoreProductDto(product));
        if(fakeStoreProductDto != null)
            return getProduct(fakeStoreProductDto);
        return null;
    }

    @Override
    public Product updateProduct(Long id, Product product) {
        FakeStoreProductDto fakeStoreProductDto = fakeStoreClient.updateProduct(id, getFakeStoreProductDto(product));
        if(fakeStoreProductDto != null)
            return getProduct(fakeStoreProductDto);
        return null;
    }

    private Product getProduct(FakeStoreProductDto fakeStoreProductDto) {
        Product product = new Product();
        product.setId(fakeStoreProductDto.getId());
        product.setName(fakeStoreProductDto.getTitle());
        product.setDescription(fakeStoreProductDto.getDescription());
        product.setPrice(fakeStoreProductDto.getPrice());
        product.setImageUrl(fakeStoreProductDto.getImage());
        Category category = new Category();
        category.setName(fakeStoreProductDto.getCategory());
        product.setCategory(category);
        return product;
    }

    private FakeStoreProductDto getFakeStoreProductDto(Product product) {
        FakeStoreProductDto fakeStoreProductDto = new FakeStoreProductDto();
        fakeStoreProductDto.setId(product.getId());
        fakeStoreProductDto.setTitle(product.getName());
        fakeStoreProductDto.setDescription(product.getDescription());
        fakeStoreProductDto.setPrice(product.getPrice());
        fakeStoreProductDto.setImage(product.getImageUrl());
        if(product.getCategory() != null)
            fakeStoreProductDto.setCategory(product.getCategory().getName());
        return fakeStoreProductDto;
    }
}
