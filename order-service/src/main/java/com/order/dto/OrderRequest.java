package com.order.dto;

import com.order.Constant.OrderStatus;
import com.order.model.OrderItem;
import jakarta.persistence.CascadeType;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.OneToMany;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderRequest {
    private Long userId;
    private String shippingAddress;
    private String paymentMethod;
    private List<OrderItemRequest> orderItems = new ArrayList<>();
}
