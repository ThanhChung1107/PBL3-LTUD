package com.ProjectPBL3.MegarMart.Repository;

import com.ProjectPBL3.MegarMart.Entity.Account;
import com.ProjectPBL3.MegarMart.Entity.Product;
import com.ProjectPBL3.MegarMart.Entity.ReviewProduct;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReviewProductRepository extends JpaRepository<ReviewProduct,Integer>, JpaSpecificationExecutor<ReviewProduct> {
    boolean existsByAccountAndProduct(Account account, Product product);
    List<ReviewProduct> findByProduct(Product product);
    @Query("SELECT r FROM ReviewProduct r WHERE r.product.shop.id = :shopId")
    List<ReviewProduct> findAllByShopId(@Param("shopId") Integer shopId);
    List<ReviewProduct> findByProductId(int productId);
    @Query("SELECT r FROM ReviewProduct r WHERE r.product.id = :productId")
    List<ReviewProduct> findReviewsByProductId(@Param("productId") int productId);
}
