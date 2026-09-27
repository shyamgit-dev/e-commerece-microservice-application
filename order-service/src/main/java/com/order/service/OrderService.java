package com.order.service;

import com.order.dto.OrderRequest;
import com.order.dto.OrderResponse;

import java.util.List;

public interface OrderService {
    OrderResponse createOrder(OrderRequest orderRequest);
    OrderResponse getOrder(Long orderId);
    List<OrderResponse> getOrders(List<Long> orderIds);
    void cancelOrder();
    void deleteOrder(Long orderId);
}
