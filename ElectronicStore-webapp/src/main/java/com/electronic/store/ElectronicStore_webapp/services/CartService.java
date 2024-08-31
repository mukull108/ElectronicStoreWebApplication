package com.electronic.store.ElectronicStore_webapp.services;

import com.electronic.store.ElectronicStore_webapp.dtos.AddItemToCartRequest;
import com.electronic.store.ElectronicStore_webapp.dtos.CartDto;
import com.electronic.store.ElectronicStore_webapp.dtos.CartItemDto;

public interface CartService {
    //add item to cart
    //if cart is not available for the user then create the cart
    //else add the items to cart
    CartDto addItemToCart(String userId, AddItemToCartRequest item);
    //remove item from cart
    void removeItemFromCart(String userId, int cartItem);

    //clear cart
    void clearCart(String userId);

    //
    CartDto getCartByUser(String userId);

}
