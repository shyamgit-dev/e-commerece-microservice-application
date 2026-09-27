package com.product.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.SoftDelete;
import org.hibernate.annotations.SoftDeleteType;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@SoftDelete(columnName = "is_active",strategy = SoftDeleteType.ACTIVE)
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private String description;

    private BigDecimal price;

    @ColumnDefault("true")
    @Column(name = "is_active",insertable = false,updatable = false)
    private boolean active;

    private Integer stockQuantity;

    private String category;

    private double averageRating;

    private LocalDateTime createdAt;

    private LocalDateTime modifiedAt;
}
