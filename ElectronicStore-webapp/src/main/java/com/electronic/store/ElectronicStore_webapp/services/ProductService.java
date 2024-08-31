package com.electronic.store.ElectronicStore_webapp.services;

import com.electronic.store.ElectronicStore_webapp.dtos.PageableResponse;
import com.electronic.store.ElectronicStore_webapp.dtos.ProductDto;
import org.springframework.stereotype.Service;

import java.util.List;

public interface ProductService {
    //add product
    ProductDto addProduct(ProductDto productDto);
    //delete a product
    void removeProduct(String id);
    //update product details
    ProductDto updateProduct(ProductDto productDto, String id);
    //get a product
    ProductDto getProduct(String id);
    //get all products
    PageableResponse<ProductDto> getAllProduct(int pageNumber, int pageSize, String sortBy, String sortDir);
    //search all live products
    PageableResponse<ProductDto> getAllLive(int pageNumber, int pageSize, String sortBy, String sortDir);
    //search with a keyword
    PageableResponse<ProductDto> searchByTitle(String subtitle, int pageNumber, int pageSize, String sortBy, String sortDir);

    //create product with category
    ProductDto createWithCategory(ProductDto productDto, String categoryId);
    //update category of a product
    ProductDto updateCategory(String productId, String categoryId);

    //get products by category
    PageableResponse<ProductDto> getAllProductByCategory(int pageNumber, int pageSize, String sortBy
                                                         ,String sortDir,String categoryId);

}
