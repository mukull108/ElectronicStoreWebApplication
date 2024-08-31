package com.electronic.store.ElectronicStore_webapp.dtos;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

import java.util.Date;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@ToString
public class CreateOrderRequest {
    @NotBlank(message = "user id is required")
    private String userId;
    @NotBlank(message = "cart id is required")
    private String cartId;
    private String orderStatus = "PENDING";
    private String paymentStatus = "NOT PAID";
    @NotBlank(message = "address is required")
    private String billingAddress;
    @NotBlank(message = "phone number is required")
    private String billingPhoneNumber;
    @NotBlank(message = "billing name is required")
    private String billingName;
    private Date deliveredDate = null;
}
