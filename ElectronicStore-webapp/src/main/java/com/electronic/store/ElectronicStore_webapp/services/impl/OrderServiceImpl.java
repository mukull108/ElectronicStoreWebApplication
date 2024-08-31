package com.electronic.store.ElectronicStore_webapp.services.impl;

import com.electronic.store.ElectronicStore_webapp.dtos.CreateOrderRequest;
import com.electronic.store.ElectronicStore_webapp.dtos.OrderDto;
import com.electronic.store.ElectronicStore_webapp.dtos.PageableResponse;
import com.electronic.store.ElectronicStore_webapp.entities.*;
import com.electronic.store.ElectronicStore_webapp.exceptions.BadApiRequestException;
import com.electronic.store.ElectronicStore_webapp.exceptions.ResourceNotFoundException;
import com.electronic.store.ElectronicStore_webapp.helper.Helper;
import com.electronic.store.ElectronicStore_webapp.repositories.CartRepository;
import com.electronic.store.ElectronicStore_webapp.repositories.OrderRepository;
import com.electronic.store.ElectronicStore_webapp.repositories.UserRepository;
import com.electronic.store.ElectronicStore_webapp.services.OrderService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

@Service
public class OrderServiceImpl implements OrderService {
    @Autowired
    private OrderRepository orderRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private CartRepository cartRepository;
    @Autowired
    private ModelMapper mapper;
    @Override
    public OrderDto createOrder(CreateOrderRequest orderDto) {
        String userId = orderDto.getUserId();
        String cartId = orderDto.getCartId();

        //fetch user by id
        User user = userRepository.findById(userId).orElseThrow(() -> new ResourceNotFoundException("User not found !!"));
        //fetch cart
        Cart cart = cartRepository.findById(cartId).orElseThrow(() -> new ResourceNotFoundException("Cart not found with given cart id"));
        List<CartItem> itemList = cart.getItemList();
        if(itemList.isEmpty()){
            throw new BadApiRequestException("No items found in cart!!");
        }

        //generate order now
        Order order = Order.builder()
                .billingName(orderDto.getBillingName())
                .billingPhoneNumber(orderDto.getBillingPhoneNumber())
                .billingAddress(orderDto.getBillingAddress())
                .orderedDate(new Date())
                .deliveredDate(orderDto.getDeliveredDate())
                .paymentStatus(orderDto.getPaymentStatus())
                .orderStatus(orderDto.getOrderStatus())
                .orderId(UUID.randomUUID().toString())
                .user(user)
                .build(); //orderItems and amount not set

        //will get all the cart items and will change those to order items
        AtomicReference<Integer> orderAmount = new AtomicReference<>(0);
        List<OrderItem> orderItems = itemList.stream().map(item -> {

            //convert cart item to order item and then return
            OrderItem orderItem = OrderItem.builder()
                    .quantity(item.getQuantity())
                    .product(item.getProduct())
                    .totalPrice((int) (item.getQuantity() * item.getProduct().getDiscountedPrice()))
                    .order(order)
                    .build();
            orderAmount.set(orderAmount.get() + orderItem.getTotalPrice());
            return orderItem;
        }).collect(Collectors.toList());
        order.setOrderItemList(orderItems);
        order.setOrderAmount(orderAmount.get());
        //clear cart as items moved to order
        cart.getItemList().clear();

        cartRepository.save(cart);
        Order savedOrder = orderRepository.save(order);

        return mapper.map(savedOrder, OrderDto.class);
    }

    @Override
    public void removeOrder(String orderId) {
        Order order = orderRepository.findById(orderId).orElseThrow(() -> new ResourceNotFoundException("Order not found with given id!!"));
        orderRepository.delete(order);
    }

    @Override
    public List<OrderDto> getOrdersOfUser(String userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new ResourceNotFoundException("user not found !!"));
        List<Order> orderList = orderRepository.findByUser(user);
        List<OrderDto> orderDtos = orderList.stream().map(order ->
                mapper.map(order, OrderDto.class)).collect(Collectors.toList());

        return orderDtos;
    }

    @Override
    public PageableResponse<OrderDto> getAllOrders(int pageNumber, int pageSize, String sortBy, String sortDir) {
        Sort sort = (sortDir.equalsIgnoreCase("desc")) ? (Sort.by(sortBy).descending()) : (Sort.by(sortBy).ascending());
        Pageable pageable = PageRequest.of(pageNumber,pageSize,sort);
        Page<Order> page = orderRepository.findAll(pageable);

        return Helper.getPageableResponse(page, OrderDto.class);
    }
}
