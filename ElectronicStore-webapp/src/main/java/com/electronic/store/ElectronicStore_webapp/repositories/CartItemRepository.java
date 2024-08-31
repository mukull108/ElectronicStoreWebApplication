package com.electronic.store.ElectronicStore_webapp.repositories;

import com.electronic.store.ElectronicStore_webapp.entities.CartItem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CartItemRepository extends JpaRepository<CartItem,Integer> {
}
