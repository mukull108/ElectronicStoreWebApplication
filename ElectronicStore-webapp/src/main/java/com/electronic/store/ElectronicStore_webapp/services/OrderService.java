package com.electronic.store.ElectronicStore_webapp.services;

import com.electronic.store.ElectronicStore_webapp.dtos.CreateOrderRequest;
import com.electronic.store.ElectronicStore_webapp.dtos.OrderDto;
import com.electronic.store.ElectronicStore_webapp.dtos.PageableResponse;

import java.util.List;

public interface OrderService {
    //create order
    OrderDto createOrder(CreateOrderRequest orderDto);
    //remove order
    void removeOrder(String orderId);
    //get order of user
    List<OrderDto> getOrdersOfUser(String userId);
    //get orders
    PageableResponse<OrderDto> getAllOrders(int pageNumber,int pageSize, String sortBy, String sortDir);

}
