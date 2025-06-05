package com.ProjectPBL3.MegarMart.Repository;

import com.ProjectPBL3.MegarMart.Entity.Account;
import com.ProjectPBL3.MegarMart.Entity.Orders;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface OrdersRepository extends JpaRepository<Orders,Integer> {
    List<Orders> findByAccount(Account account);
    @Query(value = "SELECT IFNULL(SUM(od.price * od.quantity), 0) FROM order_detail od " +
            "JOIN product p ON od.product_id = p.id " +
            "JOIN orders o ON od.order_id = o.id " +
            "WHERE p.shop_id = :shopId AND o.is_paid = 1", nativeQuery = true)
    Double sumRevenueByShop(@Param("shopId") Integer shopId);

    @Query("SELECT COALESCE(SUM(od.price * od.quantity), 0) FROM OrderDetail od " +
            "WHERE od.product.shop.id = :shopId AND od.order.isPaid = 1 " +
            "AND od.order.createdAt BETWEEN :start AND :end")
    Double sumRevenueByShopAndDateRange(@Param("shopId") Integer shopId,
                                        @Param("start") LocalDate start,
                                        @Param("end") LocalDate end);
    @Query(value = """
        SELECT COUNT(DISTINCT od.order_id)
        FROM order_detail od
        JOIN product p ON od.product_id = p.id
        JOIN orders o ON od.order_id = o.id
        WHERE p.shop_id = :shopId
          AND MONTH(o.created_at) = MONTH(CURRENT_DATE())
          AND YEAR(o.created_at) = YEAR(CURRENT_DATE())
        """, nativeQuery = true)
    int countOrdersThisMonthByShop(@Param("shopId") Integer shopId);
    @Query("SELECT COUNT(DISTINCT o.id) FROM Orders o " +
            "JOIN o.orderDetails od WHERE od.product.shop.id = :shopId")
    int countByShop(@Param("shopId") Integer shopId);

    @Query("SELECT COUNT(DISTINCT o.id) FROM Orders o " +
            "JOIN o.orderDetails od WHERE od.product.shop.id = :shopId " +
            "AND o.createdAt BETWEEN :start AND :end")
    int countByShopAndDateRange(@Param("shopId") Integer shopId,
                                @Param("start") LocalDate start,
                                @Param("end") LocalDate end);
}
