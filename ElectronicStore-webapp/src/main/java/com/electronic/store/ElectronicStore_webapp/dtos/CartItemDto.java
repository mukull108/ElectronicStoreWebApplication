package com.electronic.store.ElectronicStore_webapp.dtos;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CartItemDto {
    private int cartItemId;
    private int quantity;
    private double totalPrice;
    private ProductDto product;
}
