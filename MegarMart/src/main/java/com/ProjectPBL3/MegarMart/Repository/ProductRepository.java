package com.ProjectPBL3.MegarMart.Repository;

import com.ProjectPBL3.MegarMart.Entity.Product;
import com.ProjectPBL3.MegarMart.Entity.Shop;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product,Integer> {
    List<Product> findByStatus(int status);
    List<Product> findByShop(Shop shop);
    List<Product> findByShopAndStatus(Shop shop, int status);

    @Query("SELECT COUNT(*) FROM Product p WHERE p.shop.id = :shopId")
    int countByShopId(@Param("shopId") int shopid);
}
