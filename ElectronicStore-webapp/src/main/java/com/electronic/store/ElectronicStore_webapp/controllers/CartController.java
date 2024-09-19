package com.electronic.store.ElectronicStore_webapp.controllers;

import com.electronic.store.ElectronicStore_webapp.dtos.AddItemToCartRequest;
import com.electronic.store.ElectronicStore_webapp.dtos.ApiResponseMessages;
import com.electronic.store.ElectronicStore_webapp.dtos.CartDto;
import com.electronic.store.ElectronicStore_webapp.dtos.CartItemDto;
import com.electronic.store.ElectronicStore_webapp.services.CartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/carts")
public class CartController {

    @Autowired
    private CartService cartService;

    @PreAuthorize("hasAnyRole('ADMIN','NORMAL')")
    @PostMapping("/addItem/{userId}")
    public ResponseEntity<CartDto> addItemToCart(@PathVariable String userId,
                                                     @RequestBody AddItemToCartRequest request){

        CartDto cartDto = cartService.addItemToCart(userId, request);
        return new ResponseEntity<>(cartDto, HttpStatus.OK);
    }

    @PreAuthorize("hasAnyRole('ADMIN','NORMAL')")
    @DeleteMapping("/{userId}/items/{itemId}")
    public ResponseEntity<ApiResponseMessages> removeItemFromCart(
            @PathVariable String userId,
            @PathVariable int itemId
    ){
        cartService.removeItemFromCart(userId,itemId);
        ApiResponseMessages responseMessages = ApiResponseMessages.builder()
                .message("Item has removed!!")
                .success(true)
                .status(HttpStatus.OK)
                .build();

        return new ResponseEntity<>(responseMessages,HttpStatus.OK);
    }

    @PreAuthorize("hasAnyRole('ADMIN','NORMAL')")
    @DeleteMapping("/{userId}")
    public ResponseEntity<ApiResponseMessages> clearCart(
            @PathVariable String userId
    ){
        cartService.clearCart(userId);
        ApiResponseMessages responseMessages = ApiResponseMessages.builder()
                .status(HttpStatus.OK)
                .success(true)
                .message("Cart is clear now!!")
                .build();
        return new ResponseEntity<>(responseMessages,HttpStatus.OK);
    }

    @PreAuthorize("hasAnyRole('ADMIN','NORMAL')")
    @GetMapping("/{userId}")
    public  ResponseEntity<CartDto> getCart(
            @PathVariable String userId
    ){
        CartDto cartByUser = cartService.getCartByUser(userId);
        return new ResponseEntity<>(cartByUser,HttpStatus.OK);
    }

}
