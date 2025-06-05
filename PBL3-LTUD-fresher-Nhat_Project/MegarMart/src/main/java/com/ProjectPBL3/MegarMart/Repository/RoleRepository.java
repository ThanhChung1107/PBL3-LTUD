package com.ProjectPBL3.MegarMart.Repository;


import com.ProjectPBL3.MegarMart.Entity.Role;

import org.springframework.data.jpa.repository.JpaRepository;



public interface RoleRepository extends JpaRepository<Role,Integer> {
    Role findByRoleName(String roleName);
}
