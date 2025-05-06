package com.ProjectPBL3.MegarMart.Repository;

import com.ProjectPBL3.MegarMart.Entity.OrderDetail;
import com.ProjectPBL3.MegarMart.Entity.Product;
import com.ProjectPBL3.MegarMart.Entity.Shop;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface OrderDetailRepository extends JpaRepository<OrderDetail, Integer> {
    List<OrderDetail> findByProduct(Product product);
    boolean existsByProductId(Integer productId);
    @Query("SELECT od FROM OrderDetail od WHERE od.product.shop = :shop ORDER BY od.order.createdAt DESC")
    Page<OrderDetail> findOrderDetailsByShop(@Param("shop") Shop shop, Pageable pageable);
    @Query("SELECT od FROM OrderDetail od WHERE od.product.shop = :shop ORDER BY od.order.createdAt DESC")
    List<OrderDetail> findOrderDetailsByShop(@Param("shop") Shop shop);
    // Không phân trang
    @Query("""
            SELECT od FROM OrderDetail od 
            WHERE od.product.shop = :shop 
            AND (:keyword IS NULL OR LOWER(od.product.name) LIKE LOWER(CONCAT('%', :keyword, '%')))
            AND (:fromDate IS NULL OR od.order.createdAt >= :fromDate)
            AND (:toDate IS NULL OR od.order.createdAt <= :toDate)
            ORDER BY od.order.createdAt DESC
           """)
    List<OrderDetail> findFilteredOrderDetails(
            @Param("shop") Shop shop,
            @Param("keyword") String keyword,
            @Param("fromDate") LocalDate fromDate,
            @Param("toDate") LocalDate toDate
    );

    // Có phân trang
    @Query("""
            SELECT od FROM OrderDetail od 
            WHERE od.product.shop = :shop 
            AND (:keyword IS NULL OR LOWER(od.product.name) LIKE LOWER(CONCAT('%', :keyword, '%')))
            AND (:fromDate IS NULL OR od.order.createdAt >= :fromDate)
            AND (:toDate IS NULL OR od.order.createdAt <= :toDate)
            ORDER BY od.order.createdAt DESC
           """)
    Page<OrderDetail> findFilteredOrderDetailsPage(
            @Param("shop") Shop shop,
            @Param("keyword") String keyword,
            @Param("fromDate") LocalDate fromDate,
            @Param("toDate") LocalDate toDate,
            Pageable pageable
    );
}

