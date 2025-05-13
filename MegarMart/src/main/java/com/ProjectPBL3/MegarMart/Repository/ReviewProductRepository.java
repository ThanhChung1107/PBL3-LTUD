package com.ProjectPBL3.MegarMart.Repository;

import com.ProjectPBL3.MegarMart.Entity.Account;
import com.ProjectPBL3.MegarMart.Entity.Product;
import com.ProjectPBL3.MegarMart.Entity.RatingLevel;
import com.ProjectPBL3.MegarMart.Entity.ReviewProduct;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.data.domain.*;


import java.time.LocalDate;
import java.util.List;

@Repository
public interface ReviewProductRepository extends JpaRepository<ReviewProduct,Integer>, JpaSpecificationExecutor<ReviewProduct> {
    boolean existsByAccountAndProduct(Account account, Product product);
    List<ReviewProduct> findByProduct(Product product);
    @Query("SELECT r FROM ReviewProduct r WHERE r.product.shop.id = :shopId")
    List<ReviewProduct> findAllByShopId(@Param("shopId") Integer shopId);
    @Query("SELECT r FROM ReviewProduct r WHERE r.product.shop.id = :shopId")
    Page<ReviewProduct> findAllByShopId(@Param("shopId") Integer shopId , Pageable pageable );
    List<ReviewProduct> findByProductId(int productId);

    @Query("SELECT r FROM ReviewProduct r WHERE " +
            "r.product.shop.id = :shopId " +
            "AND (:rating IS NULL OR r.rating = :rating) " +
            "AND (:startDate IS NULL OR r.createdAt >= :startDate) " +
            "AND (:endDate IS NULL OR r.createdAt <= :endDate) " +
            "AND (:keyword IS NULL OR LOWER(r.product.name) LIKE LOWER(CONCAT('%', :keyword, '%')))")
    Page<ReviewProduct> filterReviews(
            @Param("shopId") Integer shopId,
            @Param("rating") RatingLevel rating,
            @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate,
            @Param("keyword") String keyword,
            Pageable pageable
    );
    @Query("SELECT r FROM ReviewProduct r WHERE r.product.id = :productId")
    List<ReviewProduct> findReviewsByProductId(@Param("productId") int productId);

    @Query(value = "SELECT AVG(r.rating + 1) FROM review_product r " +
            "JOIN product p ON r.product_id = p.id " +
            "WHERE p.shop_id = :shopId", nativeQuery = true)
    Double averageRatingByShop(@Param("shopId") Integer shopId);

    @Query("SELECT r FROM ReviewProduct r WHERE r.product.shop.id = :shopId " +
            "ORDER BY r.createdAt DESC")
    List<ReviewProduct> findRecentByShop(@Param("shopId") Integer shopId, Pageable pageable);
}
