package com.order.dto;

import com.order.model.Orders;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderItemResponse {
    private Long Id;
    private Long productId;
    private Integer quantity;
    private BigDecimal unitPrice;
    private String productName;
}
