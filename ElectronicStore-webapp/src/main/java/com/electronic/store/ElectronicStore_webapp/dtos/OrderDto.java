package com.electronic.store.ElectronicStore_webapp.dtos;

import com.electronic.store.ElectronicStore_webapp.entities.OrderItem;
import com.electronic.store.ElectronicStore_webapp.entities.User;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class OrderDto {
    private String orderId;
    private String orderStatus = "PENDING";
    private String paymentStatus = "NOT PAID";
    private int orderAmount;
    private String billingAddress;
    private String billingPhoneNumber;
    private String billingName;
    private Date orderedDate = new Date();
    private Date deliveredDate;
//    private UserDto userDto;
    private List<OrderItemDto> orderItemList = new ArrayList<>();

}
