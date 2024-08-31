package com.electronic.store.ElectronicStore_webapp.services.impl;

import com.electronic.store.ElectronicStore_webapp.dtos.AddItemToCartRequest;
import com.electronic.store.ElectronicStore_webapp.dtos.CartDto;
import com.electronic.store.ElectronicStore_webapp.entities.Cart;
import com.electronic.store.ElectronicStore_webapp.entities.CartItem;
import com.electronic.store.ElectronicStore_webapp.entities.Product;
import com.electronic.store.ElectronicStore_webapp.entities.User;
import com.electronic.store.ElectronicStore_webapp.exceptions.ResourceNotFoundException;
import com.electronic.store.ElectronicStore_webapp.repositories.CartItemRepository;
import com.electronic.store.ElectronicStore_webapp.repositories.CartRepository;
import com.electronic.store.ElectronicStore_webapp.repositories.ProductRepository;
import com.electronic.store.ElectronicStore_webapp.repositories.UserRepository;
import com.electronic.store.ElectronicStore_webapp.services.CartService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

@Service
public class CartServiceImpl implements CartService {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CartRepository cartRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private ModelMapper mapper;

    @Autowired
    private CartItemRepository cartItemRepository;

    @Override
    public CartDto addItemToCart(String userId, AddItemToCartRequest item) {
        String productId = item.getProductId();
        int quantity = item.getQuantity();

        User user = userRepository.findById(userId).orElseThrow(() -> new ResourceNotFoundException("user not found !!"));
        Product product = productRepository.findById(productId).orElseThrow(() -> new ResourceNotFoundException("Product not found !!"));

        // Find the cart for the user or create a new one if not found
        Cart cart = cartRepository.findByUser(user).orElseGet(() -> {
            Cart newCart = new Cart();
            newCart.setCartId(UUID.randomUUID().toString());
            newCart.setAddedDate(new Date());
            return newCart;
        });

        //if item is present then increase the quantity only
        List<CartItem> itemList = cart.getItemList();
        AtomicReference<Boolean> updated = new AtomicReference<>(false);

        itemList = itemList.stream().map(item1 -> {
            if (item1.getProduct().getProductId().equals(productId)) {
                //already present in cart
                item1.setQuantity(quantity);
                item1.setTotalPrice(quantity * product.getDiscountedPrice());
                updated.set(true);
            }
            return item1;
        }).collect(Collectors.toList());

        //create Items
        if (!updated.get()) {
            CartItem cartItem = CartItem.builder()
                    .product(product)
                    .quantity(quantity)
                    .cart(cart)
                    .totalPrice(quantity * product.getDiscountedPrice())
                    .build();

            cart.getItemList().add(cartItem);
        }

        cart.setUser(user);
        Cart updatedCart = cartRepository.save(cart);
        return mapper.map(updatedCart, CartDto.class);
    }

    @Override
    public void removeItemFromCart(String userId, int cartItem) {
        CartItem cartItem1 = cartItemRepository.findById(cartItem).orElseThrow(() -> new ResourceNotFoundException("Cart item not found in database !!"));
        cartItemRepository.delete(cartItem1);
    }

    @Override
    public void clearCart(String userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new ResourceNotFoundException());
        Cart cart = cartRepository.findByUser(user).orElseThrow(() -> new ResourceNotFoundException("Cart not found for the given user"));
        cart.getItemList().clear();
        cartRepository.save(cart);
    }

    @Override
    public CartDto getCartByUser(String userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new ResourceNotFoundException());
        Cart cart = cartRepository.findByUser(user).orElseThrow(() -> new ResourceNotFoundException("Cart not found for the given user"));
        return mapper.map(cart, CartDto.class);
    }
}
