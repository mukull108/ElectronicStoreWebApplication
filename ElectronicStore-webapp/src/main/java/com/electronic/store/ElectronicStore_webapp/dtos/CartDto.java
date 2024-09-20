package com.electronic.store.ElectronicStore_webapp.dtos;

import com.electronic.store.ElectronicStore_webapp.entities.CartItem;
import com.electronic.store.ElectronicStore_webapp.entities.User;
import jakarta.persistence.*;
import lombok.*;

import java.util.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CartDto {
    private String cartId;
    private Date addedDate;
    private UserDto user;
    private List<CartItemDto> itemList = new ArrayList<>();

}
