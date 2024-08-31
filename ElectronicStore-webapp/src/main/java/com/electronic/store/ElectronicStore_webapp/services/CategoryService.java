package com.electronic.store.ElectronicStore_webapp.services;

import com.electronic.store.ElectronicStore_webapp.dtos.CategoryDto;
import com.electronic.store.ElectronicStore_webapp.dtos.PageableResponse;

public interface CategoryService {
    //create
    CategoryDto createCategory(CategoryDto categoryDto);

    //update
    CategoryDto updateCategory(CategoryDto categoryDto, String id);

    //delete
    void deleteCategory(String id);

    //select one
    CategoryDto getCategory(String id);

    //select all
    PageableResponse<CategoryDto> getAllCategory(int pageNumber, int pageSize,String sortBy,
                                                 String sortDir);


}
