package com.electronic.store.ElectronicStore_webapp.entities;

import jakarta.persistence.*;
import lombok.*;

import java.util.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
public class Cart {
    @Id
    private String cartId;
    private Date addedDate;

    @OneToOne
    private User user;

    //cartItems mapping by-directional
    @OneToMany(mappedBy = "cart", cascade = CascadeType.ALL,fetch = FetchType.EAGER,orphanRemoval = true)
    private List<CartItem> itemList = new ArrayList<>();

}
