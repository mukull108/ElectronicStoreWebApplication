package com.electronic.store.ElectronicStore_webapp.controllers;


import com.electronic.store.ElectronicStore_webapp.dtos.*;
import com.electronic.store.ElectronicStore_webapp.services.FileService;
import com.electronic.store.ElectronicStore_webapp.services.ProductService;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StreamUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;

@RestController
@RequestMapping("/product")
public class ProductController {
    @Autowired
    private ProductService productService;
    @Autowired
    private FileService fileService;
    @Value("${product.image.path}")
    private String imagePath;
    Logger logger = LoggerFactory.getLogger(ProductController.class);

    //add product api
    @PostMapping("/create")
    public ResponseEntity<ProductDto> addProductRequestHandler(@RequestBody ProductDto productDto){
        ProductDto productDto1 = productService.addProduct(productDto);
        return new ResponseEntity<>(productDto1, HttpStatus.CREATED);
    }
    //delete a product api
    @DeleteMapping("/delete/{productId}")
    public ResponseEntity<ApiResponseMessages> deleteProductRequestHandler(
            @PathVariable String productId
    ){

        productService.removeProduct(productId);
        ApiResponseMessages responseMessages = ApiResponseMessages.builder()
                .message("Product with the given Id deleted successfully!!")
                .success(true).status(HttpStatus.OK).build();
        return new ResponseEntity<>(responseMessages,HttpStatus.OK);
    }
    //update product details api
    @PutMapping("/update/{productId}")
    public ResponseEntity<ProductDto> updateProductRequestHandler(
            @RequestBody ProductDto productDto,
            @PathVariable String productId
    ){
        ProductDto productDto1 = productService.updateProduct(productDto, productId);
        return new ResponseEntity<>(productDto1,HttpStatus.OK);
    }
    //get a product api
    @GetMapping("/get/{productId}")
    public ResponseEntity<ProductDto> getProductRequestHandler(
            @PathVariable String productId
    ){
        ProductDto product = productService.getProduct(productId);
        return new ResponseEntity<>(product,HttpStatus.OK);
    }
    //get all products api
    @GetMapping("/getAll")
    public ResponseEntity<PageableResponse<ProductDto>> getAllProductRequestHandler(
            @RequestParam(value = "pageNumber", defaultValue = "0", required = false) int pageNumber,
            @RequestParam(value = "pageSize", defaultValue = "10", required = false) int pageSize,
            @RequestParam(value = "sortBy", defaultValue = "title", required = false) String sortBy,
            @RequestParam(value = "sortDir", defaultValue = "asc", required = false) String sortDir
    ){
        PageableResponse<ProductDto> allProduct = productService.getAllProduct(pageNumber, pageSize, sortBy, sortDir);
        return new ResponseEntity<>(allProduct,HttpStatus.OK);
    }
    //search all live products api
    @GetMapping("/live")
    public ResponseEntity<PageableResponse<ProductDto>> getLiveProductRequestHandler(
            @RequestParam(value = "pageNumber", defaultValue = "0", required = false) int pageNumber,
            @RequestParam(value = "pageSize", defaultValue = "10", required = false) int pageSize,
            @RequestParam(value = "sortBy", defaultValue = "title", required = false) String sortBy,
            @RequestParam(value = "sortDir", defaultValue = "asc", required = false) String sortDir
    ){
        PageableResponse<ProductDto> allProduct = productService.getAllLive(pageNumber, pageSize, sortBy, sortDir);
        return new ResponseEntity<>(allProduct,HttpStatus.OK);
    }
    //search with a keyword api
    @GetMapping("/search/{keyword}")
    public ResponseEntity<PageableResponse<ProductDto>> searchByTitleRequestHandler(
            @RequestParam(value = "pageNumber", defaultValue = "0", required = false) int pageNumber,
            @RequestParam(value = "pageSize", defaultValue = "10", required = false) int pageSize,
            @RequestParam(value = "sortBy", defaultValue = "title", required = false) String sortBy,
            @RequestParam(value = "sortDir", defaultValue = "asc", required = false) String sortDir,
            @PathVariable String keyword
    ){
        PageableResponse<ProductDto> allProduct = productService
                .searchByTitle(keyword,pageNumber, pageSize, sortBy, sortDir);
        return new ResponseEntity<>(allProduct,HttpStatus.OK);
    }

    //upload product image
    @PostMapping("/image/{productId}")
    public ResponseEntity<ImageResponse> uploadImageRequestHandler(
            @PathVariable String productId,
            @RequestParam("productImage") MultipartFile image
            ) throws IOException {

        String fileName = fileService.uploadFile(image, imagePath);
        ProductDto productDto = productService.getProduct(productId);
        productDto.setProductImageName(fileName);
        ProductDto updatedProduct = productService.updateProduct(productDto, productId);

        ImageResponse response = ImageResponse.builder().fileName(updatedProduct.getProductImageName())
                .success(true).message("file updated").build();
        return new ResponseEntity<>(response,HttpStatus.CREATED);
    }

    //serve product image
    @GetMapping("/image/{productId}")
    public void serveProductImageRequestHandler(
            @PathVariable String productId,
            HttpServletResponse response
    ) throws IOException {

        ProductDto productDto = productService.getProduct(productId);

        InputStream inputStream = fileService.getResource(imagePath,productDto.getProductImageName());
        response.setContentType(MediaType.IMAGE_JPEG_VALUE);
        StreamUtils.copy(inputStream,response.getOutputStream());
    }

    //create product with category
    @PostMapping("/{categoryId}/products")
    public ResponseEntity<ProductDto> createProductWithCategoryHandler(
            @PathVariable String categoryId,
            @RequestBody ProductDto productDto
    ){
        ProductDto withCategory = productService.createWithCategory(productDto, categoryId);
        return new ResponseEntity<>(withCategory,HttpStatus.CREATED);
    }

    @PutMapping("/{productId}/category/{categoryId}")
    public ResponseEntity<ProductDto> updateProductCategoryHandler(
            @PathVariable String productId,
            @PathVariable String categoryId
    ){
        ProductDto productDto = productService.updateCategory(productId, categoryId);
        return new ResponseEntity<>(productDto,HttpStatus.OK);
    }

    @GetMapping("/category/{categoryId}")
    public ResponseEntity<PageableResponse<ProductDto>> getProductByCategoryHandler(
            @RequestParam(value = "pageNumber", defaultValue = "0", required = false) int pageNumber,
            @RequestParam(value = "pageSize", defaultValue = "10", required = false) int pageSize,
            @RequestParam(value = "sortBy", defaultValue = "title", required = false) String sortBy,
            @RequestParam(value = "sortDir", defaultValue = "asc", required = false) String sortDir,
            @PathVariable String categoryId
    ){
        PageableResponse<ProductDto> allProductByCategory = productService
                .getAllProductByCategory(pageNumber,pageSize,sortBy,sortDir,categoryId);
        return new ResponseEntity<>(allProductByCategory,HttpStatus.OK);
    }
}
