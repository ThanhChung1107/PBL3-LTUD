package com.ProjectPBL3.MegarMart.Repository;

import com.ProjectPBL3.MegarMart.Entity.SharedProduct;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SharedProductRepository extends JpaRepository<SharedProduct, Long> {
    SharedProduct save(SharedProduct sharedProduct);
}
