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
    // Lấy tất cả sản phẩm theo shop
    @Query("SELECT p FROM Product p WHERE p.shop = :shop")
    Page<Product> findByShop(@Param("shop") Shop shop, Pageable pageable);

    // Tìm sản phẩm theo shop và tên gần đúng
    @Query("SELECT p FROM Product p WHERE p.shop = :shop AND LOWER(p.name) LIKE LOWER(CONCAT('%', :keyword, '%'))")
    Page<Product> findByShopAndNameContaining(@Param("shop") Shop shop, @Param("keyword") String keyword, Pageable pageable);

    // Lọc sản phẩm theo shop và status
    @Query("SELECT p FROM Product p WHERE p.shop = :shop AND p.status = :status")
    Page<Product> findByShopAndStatus(@Param("shop") Shop shop, @Param("status") Integer status, Pageable pageable);

    // Tìm sản phẩm theo shop, tên gần đúng và status
    @Query("SELECT p FROM Product p WHERE p.shop = :shop AND LOWER(p.name) LIKE LOWER(CONCAT('%', :keyword, '%')) AND p.status = :status")
    Page<Product> findByShopAndNameContainingAndStatus(@Param("shop") Shop shop, @Param("keyword") String keyword, @Param("status") Integer status, Pageable pageable);

    // Đếm số lượng sản phẩm theo shop và status
    @Query("SELECT COUNT(p) FROM Product p WHERE p.shop = :shop AND p.status = :status")
    long countByShopAndStatus(@Param("shop") Shop shop, @Param("status") Integer status);

    @Query("SELECT p FROM Product p " +
            "WHERE p.shop = :shop " +
            "AND (:category IS NULL OR p.category = :category) " +
            "AND p.status = 1")
    Page<Product> findByShopAndOptionalCategory(@Param("shop") Shop shop,
                                                @Param("category") Category category,
                                                Pageable pageable);

    @Query("SELECT p FROM Product p " +
            "WHERE p.shop = :shop " +
            "AND (:category IS NULL OR p.category = :category) " +
            "AND p.status = 1 " +
            "AND (LOWER(p.name) LIKE LOWER(CONCAT('%', :keyword, '%')) OR :keyword IS NULL)")
    Page<Product> findByShopAndOptionalCategoryAndKeyword(@Param("shop") Shop shop,
                                                          @Param("category") Category category,
                                                          @Param("keyword") String keyword,
                                                          Pageable pageable);
}
