package com.electronic.store.ElectronicStore_webapp.services.impl;

import com.electronic.store.ElectronicStore_webapp.dtos.CategoryDto;
import com.electronic.store.ElectronicStore_webapp.dtos.PageableResponse;
import com.electronic.store.ElectronicStore_webapp.entities.Category;
import com.electronic.store.ElectronicStore_webapp.exceptions.ResourceNotFoundException;
import com.electronic.store.ElectronicStore_webapp.helper.Helper;
import com.electronic.store.ElectronicStore_webapp.repositories.CategoryRepository;
import com.electronic.store.ElectronicStore_webapp.services.CategoryService;
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
import java.util.UUID;

@Service
public class CategoryServiceImpl implements CategoryService {

    @Autowired
    CategoryRepository categoryRepository;

    @Autowired
    private ModelMapper mapper;

    @Value("${category.image.path}")
    private String imagePath;

    Logger logger = LoggerFactory.getLogger(CategoryServiceImpl.class);

    @Override
    public CategoryDto createCategory(CategoryDto categoryDto) {
        String categoryId = UUID.randomUUID().toString();
        categoryDto.setCategoryId(categoryId);
        Category category = mapper.map(categoryDto, Category.class);
        Category savedCategory = categoryRepository.save(category);
        return mapper.map(savedCategory,CategoryDto.class);

    }

    @Override
    public CategoryDto updateCategory(CategoryDto categoryDto, String id) {
        Category category = categoryRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Category not found with given id"));

        category.setDescription(categoryDto.getDescription());
        category.setTitle(categoryDto.getTitle());
        category.setCoverImage(categoryDto.getCoverImage());

        Category updatedCategory = categoryRepository.save(category);
        return mapper.map(updatedCategory,CategoryDto.class);
    }

    @Override
    public void deleteCategory(String id) {
        Category category = categoryRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Category not found with given id"));

        String fullPath = imagePath + category.getCoverImage();

        Path path = Paths.get(fullPath);
        try {
            Files.delete(path);
        } catch (NoSuchFileException ex) {
            logger.info("Category image not found in the folder {}",fullPath);
            ex.printStackTrace();
        }
        catch (IOException e){
            e.printStackTrace();
        }
        categoryRepository.delete(category);

    }

    @Override
    public CategoryDto getCategory(String id) {
        Category category = categoryRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Category not found with the given id"));
        return mapper.map(category,CategoryDto.class);
    }

    @Override
    public PageableResponse<CategoryDto> getAllCategory(int pageNumber, int pageSize,String sortBy,
                                                        String sortDir) {

        Sort sort = (sortDir.equalsIgnoreCase("desc")) ?
                (Sort.by(sortBy).descending()) : (Sort.by(sortBy).ascending());
        Pageable pageable = PageRequest.of(pageNumber,pageSize,sort);
        Page<Category> page = categoryRepository.findAll(pageable);

        PageableResponse<CategoryDto> pageableResponse = Helper.getPageableResponse(page, CategoryDto.class);
        return pageableResponse;
    }
}
