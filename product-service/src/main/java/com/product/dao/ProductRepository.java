package com.product.dao;

import com.product.model.Product;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ProductRepository extends JpaRepository<Product,Long> {

    @Query(value = "SELECT * FROM Product p WHERE p.is_active=false",nativeQuery = true)
    List<Product> getDeletedProduct();

    @Query(value = "SELECT * FROM Product WHERE is_active=false and id= :productId",nativeQuery = true)
    Optional<Product> inactiveProduct(@Param("productId") Long productId);

    //Returns 1 if product found and updated and returns 0 if product not found and zero row updated
    // Recommended Return should be int because returns how many rows are updated
    @Modifying
    @Transactional
    @Query(value = "UPDATE Product SET is_active=true WHERE id= :productId",nativeQuery = true)
    void restoreProductById(@Param("productId") Long productId);

    @Modifying
    @Transactional
    @Query(value = "UPDATE Product p SET p.stockQuantity=p.stockQuantity- :stock "+
           "WHERE p.id=:id and p.stockQuantity>=:stock"
    )
    int decrementQuantity(@Param("id") Long productId,@Param("stock") Integer stock);
}
