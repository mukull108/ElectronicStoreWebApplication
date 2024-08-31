package com.electronic.store.ElectronicStore_webapp.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CategoryDto {

    private String categoryId;

    @NotBlank(message = "title is required")
    @Size(min = 4,message = "provide at least 4 characters")
    private String title;

    @NotBlank(message = "Description is required")
    private String description;

    private String coverImage;
}
