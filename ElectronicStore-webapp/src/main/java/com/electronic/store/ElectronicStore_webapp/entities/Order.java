package com.electronic.store.ElectronicStore_webapp.entities;


import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@Table(name = "orders")
public class Order {
    @Id
    private String orderId;

    //can be pending, ordered, dispatched, delivered
    //we can use enum also
    private String orderStatus;
    //not paid, paid
    private String paymentStatus;
    private int orderAmount;
    @Column(length = 1000)
    private String billingAddress;
    private String billingPhoneNumber;
    private String billingName;
    private Date orderedDate;
    private Date deliveredDate;

    //user
    @ManyToOne(fetch = FetchType.EAGER)
    private User user;

    @OneToMany(mappedBy = "order",fetch = FetchType.EAGER,cascade = CascadeType.ALL)
    private List<OrderItem> orderItemList = new ArrayList<>();

}
