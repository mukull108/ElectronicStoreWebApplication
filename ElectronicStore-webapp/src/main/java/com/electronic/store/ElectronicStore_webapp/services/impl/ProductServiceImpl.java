package com.electronic.store.ElectronicStore_webapp.services.impl;

import com.electronic.store.ElectronicStore_webapp.dtos.PageableResponse;
import com.electronic.store.ElectronicStore_webapp.dtos.ProductDto;
import com.electronic.store.ElectronicStore_webapp.entities.Category;
import com.electronic.store.ElectronicStore_webapp.entities.Product;
import com.electronic.store.ElectronicStore_webapp.exceptions.ResourceNotFoundException;
import com.electronic.store.ElectronicStore_webapp.helper.Helper;
import com.electronic.store.ElectronicStore_webapp.repositories.CategoryRepository;
import com.electronic.store.ElectronicStore_webapp.repositories.ProductRepository;
import com.electronic.store.ElectronicStore_webapp.services.ProductService;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.UUID;

@Service
public class ProductServiceImpl implements ProductService {
    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private CategoryRepository categoryRepository;
    @Autowired
    private ModelMapper mapper;
    @Value("${product.image.path}")
    private String imagePath;

    private Logger logger = LoggerFactory.getLogger(ProductServiceImpl.class);

    @Override
    public ProductDto addProduct(ProductDto productDto) {
        String randomId = UUID.randomUUID().toString();
        productDto.setProductId(randomId);
        Product product = mapper.map(productDto, Product.class);
        Product savedProduct = productRepository.save(product);
        return mapper.map(savedProduct, ProductDto.class);
    }

    @Override
    public void removeProduct(String id) {
        Product product = productRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Product not found with given Id !!"));

        String fullPath = imagePath + product.getProductImageName();

        Path path = Paths.get(fullPath);
        try {
            Files.delete(path);
        } catch (NoSuchFileException ex) {
            logger.info("Product image not found in the folder {}", fullPath);
            ex.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        productRepository.delete(product);
    }

    @Override
    public ProductDto updateProduct(ProductDto productDto, String id) {
        Product product = productRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Product not found with given Id !!"));
        product.setTitle(productDto.getTitle());
        product.setDescription(productDto.getDescription());
        product.setLive(productDto.isLive());
        product.setPrice(productDto.getPrice());
        product.setDiscountedPrice(productDto.getDiscountedPrice());
        product.setQuantity(productDto.getQuantity());
        product.setInStock(productDto.isInStock());
        product.setProductImageName(productDto.getProductImageName());

        Product savedProduct = productRepository.save(product);
        return mapper.map(savedProduct, ProductDto.class);
    }

    @Override
    public ProductDto getProduct(String id) {
        Product product = productRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Product not found with given Id !!"));
        return mapper.map(product, ProductDto.class);
    }

    @Override
    public PageableResponse<ProductDto> getAllProduct(int pageNumber, int pageSize, String sortBy, String sortDir) {

        Sort sort = (sortDir.equalsIgnoreCase("desc")) ? (Sort.by(sortBy).descending()) : (Sort.by(sortBy).ascending());

        Pageable pageable = PageRequest.of(pageNumber, pageSize);
        Page<Product> all = productRepository.findAll(pageable);

        return Helper.getPageableResponse(all, ProductDto.class);
    }

    @Override
    public PageableResponse<ProductDto> getAllLive(int pageNumber, int pageSize, String sortBy, String sortDir) {
        Sort sort = (sortDir.equalsIgnoreCase("desc")) ? (Sort.by(sortBy).descending()) : (Sort.by(sortBy).ascending());

        Pageable pageable = PageRequest.of(pageNumber, pageSize);
        Page<Product> all = productRepository.findByLiveTrue(pageable);

        return Helper.getPageableResponse(all, ProductDto.class);
    }

    @Override
    public PageableResponse<ProductDto> searchByTitle(String subtitle, int pageNumber, int pageSize, String sortBy, String sortDir) {
        Sort sort = (sortDir.equalsIgnoreCase("desc")) ? (Sort.by(sortBy).descending()) : (Sort.by(sortBy).ascending());

        Pageable pageable = PageRequest.of(pageNumber, pageSize);
        Page<Product> all = productRepository.findByTitleContaining(pageable, subtitle);

        return Helper.getPageableResponse(all, ProductDto.class);
    }

    @Override
    public ProductDto createWithCategory(ProductDto productDto, String categoryId) {
        Category category = categoryRepository.findById(categoryId).orElseThrow(() -> new ResourceNotFoundException("Category not found with given id"));

        String randomId = UUID.randomUUID().toString();
        productDto.setProductId(randomId);
        Product product = mapper.map(productDto, Product.class);
        product.setCategory(category);
        Product savedProduct = productRepository.save(product);
        return mapper.map(savedProduct, ProductDto.class);
    }

    @Override
    public ProductDto updateCategory(String productId, String categoryId) {
        Product product = productRepository.findById(productId).orElseThrow(() -> new ResourceNotFoundException("product not found !!"));
        Category category = categoryRepository.findById(categoryId).orElseThrow(() -> new ResourceNotFoundException("category not found !!"));

        product.setCategory(category);
        Product savedProduct = productRepository.save(product);
        return mapper.map(savedProduct, ProductDto.class);
    }

    //get all products by categoryId
    @Override
    public PageableResponse<ProductDto> getAllProductByCategory(int pageNumber, int pageSize, String sortBy, String sortDir, String categoryId) {

        Category category = categoryRepository.findById(categoryId).orElseThrow(() -> new ResourceNotFoundException("Category not found with given Id"));

        Sort sort = (sortDir.equalsIgnoreCase("desc")) ? (Sort.by(sortBy).descending()) : (Sort.by(sortBy).ascending());
        Pageable pageable = PageRequest.of(pageNumber, pageSize);

        Page<Product> page = productRepository.findByCategory(category, pageable);
        return Helper.getPageableResponse(page, ProductDto.class);
    }
}
