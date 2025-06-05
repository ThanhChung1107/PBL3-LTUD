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
    Page<OrderDetail> findByProductShopAndOrderIsPaid(Shop shop, int isPaid, Pageable pageable);
    Page<OrderDetail> findByProduct_Shop(Shop shop, Pageable pageable);
    // full list paid (không phân trang)
    List<OrderDetail> findByProductShopAndOrderIsPaid(Shop shop, int isPaid);

    // filter có paging
    @Query("SELECT od FROM OrderDetail od"+
    "  WHERE od.product.shop = :shop"+
       " AND od.order.isPaid = 1"+
       " AND (:fromDate IS NULL OR od.order.createdAt >= :fromDate)"+
       " AND (:toDate   IS NULL OR od.order.createdAt <= :toDate)"+
            "AND (:keyword IS NULL OR LOWER(od.product.name) LIKE LOWER(CONCAT('%', :keyword, '%')))"
    )
    Page<OrderDetail> findFilteredPaid(
            @Param("shop") Shop shop,
            @Param("keyword") String keyword,
            @Param("fromDate") LocalDate fromDate,
            @Param("toDate") LocalDate toDate,
            Pageable pageable
    );

    // filter full list paid
    @Query("""
      SELECT od FROM OrderDetail od
      WHERE od.product.shop = :shop
        AND od.order.isPaid = 1
        AND (:keyword IS NULL OR LOWER(od.product.name) LIKE LOWER(CONCAT('%', :keyword, '%')))
        AND (:fromDate IS NULL OR od.order.createdAt >= :fromDate)
        AND (:toDate   IS NULL OR od.order.createdAt <= :toDate)
    """)
    List<OrderDetail> findFilteredPaidNoPage(
            @Param("shop") Shop shop,
            @Param("keyword") String keyword,
            @Param("fromDate") LocalDate fromDate,
            @Param("toDate") LocalDate toDate
    );

    @Query(
  "  SELECT od FROM OrderDetail od"+
  "  WHERE od.product.shop = :shop"+
    "  AND (:status IS NULL OR od.order.isPaid = :status)"+
      "AND (:keyword IS NULL OR LOWER(od.product.name) LIKE LOWER(CONCAT('%', :keyword, '%')))"+
    "  AND (:fromDate IS NULL OR od.order.createdAt >= :fromDate)"+
   "   AND (:toDate IS NULL OR od.order.createdAt <= :toDate)"
)
    Page<OrderDetail> findFiltered(
            @Param("shop") Shop shop,
            @Param("status") Integer status,
            @Param("fromDate") LocalDate fromDate,
            @Param("toDate") LocalDate toDate,
            @Param("keyword") String keyword,
            Pageable pageable
    );
    @Query("SELECT SUM(od.price * od.quantity) " +
            "FROM OrderDetail od " +
            "WHERE od.product.shop.id = :shopId AND od.order.isPaid = 1")
    Double getTotalRevenueByShop(@Param("shopId") Integer shopId);

    @Query("SELECT COUNT(DISTINCT od.order.id) FROM OrderDetail od WHERE od.product.shop.id = :shopId")
    Integer countDistinctOrdersByShop(@Param("shopId") Integer shopId);

    // Ví dụ trả về revenue theo tháng (12 tháng gần nhất)
    @Query("SELECT FUNCTION('DATE_FORMAT', o.createdAt, '%Y-%m') AS month, SUM(od.price * od.quantity) " +
            "FROM OrderDetail od JOIN od.order o " +
            "WHERE od.product.shop.id = :shopId AND o.createdAt >= :startDate AND o.isPaid = 1 " +
            "GROUP BY FUNCTION('DATE_FORMAT', o.createdAt, '%Y-%m') " +
            "ORDER BY month ASC")
    List<Object[]> getMonthlyRevenueRawByShop(@Param("shopId") Integer shopId, @Param("startDate") LocalDate startDate);


}

