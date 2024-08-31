package com.electronic.store.ElectronicStore_webapp.controllers;

import com.electronic.store.ElectronicStore_webapp.dtos.*;
import com.electronic.store.ElectronicStore_webapp.services.CategoryService;
import com.electronic.store.ElectronicStore_webapp.services.FileService;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import jakarta.websocket.server.PathParam;
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

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.UUID;

@RestController
@RequestMapping("/category")
public class CategoryController {

    @Autowired
    private CategoryService categoryService;
    @Autowired
    private FileService fileService;

    @Value("${category.image.path}")
    private String imagePath;

    Logger logger = LoggerFactory.getLogger(CategoryController.class);

    //create
    @PostMapping("/create")
    public ResponseEntity<CategoryDto> createCategoryHandler(@Valid @RequestBody CategoryDto categoryDto){
        logger.info("inside the controller");
        CategoryDto dto = categoryService.createCategory(categoryDto);
        return new ResponseEntity<>(dto, HttpStatus.CREATED);
    }
    //update
    @PutMapping("/update/{categoryId}")
    public ResponseEntity<CategoryDto> updateCategoryHandler(@RequestBody CategoryDto categoryDto,@PathVariable String categoryId){

        CategoryDto updatedCategory = categoryService.updateCategory(categoryDto, categoryId);
        return new ResponseEntity<>(updatedCategory,HttpStatus.OK);
    }
    //delete
    @DeleteMapping("/delete/{categoryId}")
    public ResponseEntity<ApiResponseMessages> deleteCategoryHandler(@PathVariable String categoryId){
        categoryService.deleteCategory(categoryId);
        ApiResponseMessages response = ApiResponseMessages.builder().message("Category has been deleted with by provided id").status(HttpStatus.OK).success(true).build();
        return new ResponseEntity<>(response,HttpStatus.OK);
    }

    //get
    @GetMapping("/get/{categoryId}")
    public ResponseEntity<CategoryDto> getCategoryByIdHandler(@PathVariable String categoryId){
        CategoryDto categoryDto = categoryService.getCategory(categoryId);
        return new ResponseEntity<>(categoryDto,HttpStatus.OK);
    }
    //getAll
    @GetMapping("/get")
    public ResponseEntity<PageableResponse<CategoryDto>> getAllCategoryHandler(
            @RequestParam(value = "pageNumber",defaultValue = "0",required = false) int pageNumber,
            @RequestParam(value = "pageSize", defaultValue = "2", required = false) int pageSize,
            @RequestParam(value = "sortBy",defaultValue = "title",required = false) String sortBy,
            @RequestParam(value = "sortDir", defaultValue = "asc", required = false) String sortDir){

        PageableResponse<CategoryDto> allCategory =
                categoryService.getAllCategory(pageNumber, pageSize, sortBy, sortDir);
        return new ResponseEntity<>(allCategory,HttpStatus.OK);
    }

    @PostMapping("/image/{categoryId}")
    public ResponseEntity<ImageResponse> uploadCategoryImageHandler(
            @RequestParam("categoryImage") MultipartFile image,
            @PathVariable String categoryId
            ) throws IOException {

        String file = fileService.uploadFile(image, imagePath);
        logger.info("Returned File name {}", file);
        CategoryDto categoryDto = categoryService.getCategory(categoryId);
        categoryDto.setCoverImage(file);
        categoryService.updateCategory(categoryDto, categoryId);
        ImageResponse imageResponse = ImageResponse.builder()
                .fileName(file)
                .message("Image has been uploaded")
                .status(HttpStatus.CREATED)
                .success(true)
                .build();
        return new ResponseEntity<>(imageResponse,HttpStatus.OK);
    }

    @GetMapping("/image/{categoryId}")
    public void serveCategoryImageHandler(
            @PathVariable String categoryId,
            HttpServletResponse response
    ) throws IOException {

        CategoryDto categoryDto = categoryService.getCategory(categoryId);
        logger.info("Cover Image name {}", categoryDto.getCoverImage());

        InputStream inputStream = fileService.getResource(imagePath,categoryDto.getCoverImage());
        response.setContentType(MediaType.IMAGE_JPEG_VALUE);
        StreamUtils.copy(inputStream,response.getOutputStream());
    }
}
