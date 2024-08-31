package com.electronic.store.ElectronicStore_webapp.dtos;

import com.electronic.store.ElectronicStore_webapp.entities.Category;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ProductDto {

    private String productId;

    private String title;

    private String description;
    private double price;
    private String productImageName;
    private double discountedPrice;
    private int quantity;
    private boolean inStock;
    private boolean live;
    private CategoryDto category;

}
