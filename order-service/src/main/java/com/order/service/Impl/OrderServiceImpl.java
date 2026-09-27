package com.order.service.Impl;

import com.order.Constant.OrderStatus;
import com.order.dao.OrderItemRepository;
import com.order.dao.OrderRepository;
import com.order.dto.OrderItemRequest;
import com.order.dto.OrderRequest;
import com.order.dto.OrderResponse;
import com.order.exception.InvalidOrderCreationException;
import com.order.exception.OrderNotFoundException;
import com.order.model.OrderItem;
import com.order.model.Orders;
import com.order.service.OrderService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Service("orderService")
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;
    private final OrderItemRepository orderItemRepository;
    private final ModelMapper modelMapper;

    @Transactional
    @Override
    public OrderResponse createOrder(OrderRequest orderRequest) {
        Orders order = new Orders();

        BigDecimal totalAmount = BigDecimal.ZERO;

        if(orderRequest.getOrderItems()==null || orderRequest.getOrderItems().isEmpty())
        {
            throw new InvalidOrderCreationException("At least one item need to add for placing order");
        }

        for(OrderItemRequest itemRequest:orderRequest.getOrderItems())
        {
              OrderItem orderItem = new OrderItem();
              orderItem.setProductId(itemRequest.getProductId());
              orderItem.setQuantity(itemRequest.getQuantity());
              orderItem.setUnitPrice(itemRequest.getUnitPrice());
              orderItem.setOrders(order);
              orderItemRepository.save(orderItem);
              order.getOrderItems().add(orderItem);
              totalAmount = totalAmount.add(itemRequest.getUnitPrice()
                      .multiply(BigDecimal.valueOf(itemRequest.getQuantity())));
        }
        order.setOrderDate(LocalDateTime.now());
        order.setStatus(OrderStatus.CREATED);
        order.setPaymentMethod(orderRequest.getPaymentMethod());
        order.setShippingAddress(orderRequest.getShippingAddress());
        order.setUserId(orderRequest.getUserId());
        order.setTotalAmount(totalAmount);

        Orders savedOrder = orderRepository.save(order);

        log.info("Created Order {} for User{}",
                savedOrder.getOrderId(),
                savedOrder.getUserId()
                );

        return modelMapper.map(savedOrder,OrderResponse.class);
    }

    @Override
    public OrderResponse getOrder(Long orderId) {
        Orders order = orderRepository.findById(orderId)
                .orElseThrow(()->new OrderNotFoundException("Order Details Not Found"));
        log.info("Fetched Order Having Id {}",
                order.getOrderId()
                );
        return modelMapper.map(order,OrderResponse.class);
    }

    @Override
    public List<OrderResponse> getOrders(List<Long> orderIds) {
        List<Orders> orders = orderRepository.findAllById(orderIds);
        return orders.stream().map(orders1 -> modelMapper.map(orders1,OrderResponse.class)).toList();
    }

    @Override
    public void cancelOrder() {

    }

    @Override
    public void deleteOrder(Long orderId) {

    }
}
