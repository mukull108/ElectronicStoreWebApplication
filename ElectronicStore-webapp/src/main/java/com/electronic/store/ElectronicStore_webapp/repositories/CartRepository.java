package com.electronic.store.ElectronicStore_webapp.repositories;

import com.electronic.store.ElectronicStore_webapp.entities.Cart;
import com.electronic.store.ElectronicStore_webapp.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CartRepository extends JpaRepository<Cart,String> {
    Optional<Cart> findByUser(User user);
}
