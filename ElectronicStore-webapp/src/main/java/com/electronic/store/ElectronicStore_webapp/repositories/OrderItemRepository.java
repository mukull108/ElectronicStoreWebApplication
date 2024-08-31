package com.electronic.store.ElectronicStore_webapp.repositories;

import com.electronic.store.ElectronicStore_webapp.entities.OrderItem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderItemRepository extends JpaRepository<OrderItem,Integer> {
}
