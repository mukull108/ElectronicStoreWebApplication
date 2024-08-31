package com.electronic.store.ElectronicStore_webapp.entities;

import jakarta.persistence.*;
import lombok.*;

import java.util.Date;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
public class CartItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int cartItemId;
    @OneToOne
    private Product product;
    private int quantity;
    private double totalPrice;
//    private Date addedDate;

    //mapping to cart
    @ManyToOne(fetch = FetchType.LAZY)
    private Cart cart;

}
