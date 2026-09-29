package com.order.service.Impl;

import com.order.Constant.OrderStatus;
import com.order.dao.OrderItemRepository;
import com.order.dao.OrderRepository;
import com.order.dto.*;
import com.order.exception.InvalidOrderCreationException;
import com.order.exception.OrderNotFoundException;
import com.order.model.OrderItem;
import com.order.model.Orders;
import com.order.openfeign.ProductOpenClient;
import com.order.openfeign.UserOpenClient;
import com.order.service.OrderService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
@Service("orderService")
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;
    private final OrderItemRepository orderItemRepository;
    private final ModelMapper modelMapper;

    //RESTCLIENT
    private final RestClient userClient;
    private final RestClient productClient;

    //OPENFEIGN
    private final UserOpenClient userOpenClient;
    private final ProductOpenClient productOpenClient;


    @Transactional
    @Override
    public OrderResponse createOrder(OrderRequest orderRequest) {
        if(orderRequest.getOrderItems()==null || orderRequest.getOrderItems().isEmpty())
        {
            throw new InvalidOrderCreationException("At least one item is required for placing order");
        }

        //UserResponse userResponse = fetchUserById(orderRequest.getUserId());
        UserResponse userResponse = userOpenClient.fetchUserById(orderRequest.getUserId());

        log.info("Communicated with user-service and fetched user {}",
                userResponse.getUsername());

        List<Long> productIds = orderRequest.getOrderItems()
                .stream()
                .map(OrderItemRequest::getProductId)
                .distinct()
                .toList();

        //List<ProductResponse> productResponseList = fetchProductByIds(productIds);
        List<ProductResponse> productResponseList = productOpenClient.fetchProductByIds(productIds);

        log.info("Communicated with product-service and fetched product {}",
                productResponseList);

        Map<Long,ProductResponse> productMap =
                productResponseList.stream()
                        .collect(Collectors.toMap(ProductResponse::getId, Function.identity()));

        Orders order = new Orders();
        order.setOrderDate(LocalDateTime.now());
        order.setStatus(OrderStatus.CREATED);
        order.setShippingAddress(orderRequest.getShippingAddress());
        order.setUserId(userResponse.getUserId());
        order.setPaymentMethod(orderRequest.getPaymentMethod());

        if(order.getOrderItems()==null)
        {
            order.setOrderItems(new ArrayList<>());
        }

        BigDecimal amount = BigDecimal.ZERO;

        for(OrderItemRequest itemRequest: orderRequest.getOrderItems())
        {
            ProductResponse product = productMap.get(itemRequest.getProductId());
            if(product==null) throw new InvalidOrderCreationException("Product Not Found");
            OrderItem orderItem = new OrderItem();
            orderItem.setProductName(product.getName());
            orderItem.setUnitPrice(product.getPrice());
            orderItem.setProductId(product.getId());

            if(itemRequest.getQuantity()> product.getStockQuantity())
                throw new InvalidOrderCreationException("Entered quantity is greater than the stock quantity");

            //updateStock(product.getId(), itemRequest.getQuantity());
            productOpenClient.patchQuantity(product.getId(), itemRequest.getQuantity());

            orderItem.setQuantity(itemRequest.getQuantity());
            orderItem.setOrders(order);
            orderItemRepository.save(orderItem);
            order.getOrderItems().add(orderItem);

            BigDecimal itemTotal = product.getPrice().multiply(BigDecimal.valueOf(itemRequest.getQuantity()));
            amount = amount.add(itemTotal);
        }

        order.setTotalAmount(amount);
        Orders savedOrder = orderRepository.save(order);
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

    private void updateStock(Long productId,Integer updateQuantity)
    {
        String response=productClient.patch()
                .uri("/{id}/{stock}",productId,updateQuantity)
                .retrieve()
                .body(String.class);
        log.info("Stock Update Response {}",response);
    }

    private UserResponse fetchUserById(Long userId)
    {
        return userClient.get()
                .uri("/{id}",userId)
                .retrieve()
                .body(UserResponse.class);
    }

    private List<ProductResponse> fetchProductByIds(List<Long> productIds)
    {
        String idsParam = productIds.stream()
                .map(String::valueOf)
                .collect(Collectors.joining(","));

        return productClient.get()
                .uri(uriBuilder -> uriBuilder
                        .queryParam("productIds",idsParam)
                        .build()
                )
                .retrieve()
                .body(new ParameterizedTypeReference<>() {});
    }
}
