package com.ProjectPBL3.MegarMart.Repository;

import com.ProjectPBL3.MegarMart.Entity.Category;
import com.ProjectPBL3.MegarMart.Entity.Product;
import com.ProjectPBL3.MegarMart.Entity.Shop;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product,Integer> {
    List<Product> findByStatus(int status);

    List<Product> findByStatusAndCategory(int status, Category category);

    List<Product> findByShop(Shop shop);
    List<Product> findByShopAndStatus(Shop shop, int status);

    @Query("SELECT COUNT(*) FROM Product p WHERE p.shop.id = :shopId")
    int countByShopId(@Param("shopId") int shopid);

    @Query("SELECT p FROM Product p WHERE p.name LIKE %:keyword% AND p.shop = :shop")
    List<Product> searchProductByNameAndShop(@Param("keyword") String keyword, @Param("shop") Shop shop);
    @Query("SELECT p FROM Product p WHERE p.name LIKE %?1%")
    List<Product> searchProduct(String productname);

    Page<Product> findByStatusAndCategory(int status, Category category, Pageable pageable);

    @Query("SELECT p FROM Product p WHERE p.status = 1 AND p.category = :category AND " +
            "LOWER(p.name) LIKE LOWER(CONCAT('%', :keyword, '%'))")
    Page<Product> searchByKeywordAndCategory(@Param("keyword") String keyword,
                                             @Param("category") Category category,
                                             Pageable pageable);
}
