package com.electronic.store.ElectronicStore_webapp.controllers;

import com.electronic.store.ElectronicStore_webapp.dtos.ApiResponseMessages;
import com.electronic.store.ElectronicStore_webapp.dtos.CreateOrderRequest;
import com.electronic.store.ElectronicStore_webapp.dtos.OrderDto;
import com.electronic.store.ElectronicStore_webapp.dtos.PageableResponse;
import com.electronic.store.ElectronicStore_webapp.services.OrderService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/orders")
public class OrderController {
    @Autowired
    private OrderService orderService;
    //create orders
    @PostMapping
    public ResponseEntity<OrderDto> createOrderHandler(@Valid @RequestBody CreateOrderRequest request){
        OrderDto order = orderService.createOrder(request);
        return new ResponseEntity<>(order, HttpStatus.CREATED);
    }

    @DeleteMapping("/{orderId}")
    public ResponseEntity<ApiResponseMessages> removeOrderHandler(
            @PathVariable String orderId
    ){
        orderService.removeOrder(orderId);
        ApiResponseMessages messages = ApiResponseMessages.builder()
                .message("Order has been deleted with given Id")
                .success(true)
                .status(HttpStatus.OK)
                .build();
        return new ResponseEntity<>(messages,HttpStatus.OK);
    }

    @GetMapping("/users/{userId}")
    public ResponseEntity<List<OrderDto>> getOrderOfUser(@PathVariable String userId){
        List<OrderDto> ordersOfUser = orderService.getOrdersOfUser(userId);
        return new ResponseEntity<>(ordersOfUser,HttpStatus.OK);
    }


    @GetMapping
    public ResponseEntity<PageableResponse<OrderDto>> getAllOrders(
            @RequestParam(value = "pageNumber", defaultValue = "0", required = false) int pageNumber,
            @RequestParam(value = "pageSize", defaultValue = "10", required = false) int pageSize,
            @RequestParam(value = "sortBy", defaultValue = "title", required = false) String sortBy,
            @RequestParam(value = "sortDir", defaultValue = "asc", required = false) String sortDir
    ) {
        PageableResponse<OrderDto> allOrders = orderService.getAllOrders(pageNumber, pageSize, sortBy, sortDir);
        return new ResponseEntity<>(allOrders,HttpStatus.OK);
    }

    
}
