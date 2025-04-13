package com.ProjectPBL3.MegarMart.Repository;

import com.ProjectPBL3.MegarMart.Entity.Account;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.Optional;

@Repository
public interface AccountRepository extends JpaRepository<Account,Integer> {
    @EntityGraph(attributePaths = {"role"})
    Account findByUsername(String username);

    @EntityGraph(attributePaths = {"role"})
    Account findByEmail(String email);

    boolean existsByUsername(String username);
    boolean existsByEmail(String email);
    boolean existsByPhone(String phone);

    @Modifying
    @Transactional
    @Query("UPDATE Account a SET a.name = :name, a.address = :address, a.phone = :phone, a.updatedAt = :updatedAt WHERE a.id = :id")
    void updateAccountInfo(@Param("id") Integer id,
                           @Param("name") String name,
                           @Param("address") String address,
                           @Param("phone") String phone,
                           @Param("updatedAt") LocalDate updatedAt,
                           @Param("imageurl") String imageurl);
}
