package com.ProjectPBL3.MegarMart.Service;

import com.ProjectPBL3.MegarMart.Entity.Account;
import com.ProjectPBL3.MegarMart.Entity.Role;
import com.ProjectPBL3.MegarMart.Repository.AccountRepository;
import com.ProjectPBL3.MegarMart.Repository.RoleRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.hibernate.Hibernate;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AccountService {
    private final AccountRepository accountRepository;
    private final RoleRepository roleRepository;

    public boolean checkExistedUsername(String username) {
        return accountRepository.existsByUsername(username);
    }

    public boolean checkExistedEmail(String email) {return accountRepository.existsByEmail(email);}

    public boolean checkExistedPhone(String phone) {return accountRepository.existsByPhone(phone);}

    public Boolean create(Account account)
    {
        try{
            Role defaultRole = roleRepository.findById(0).orElseThrow(() -> new RuntimeException("Role not found"));
            account.setRole(defaultRole);
            accountRepository.save(account);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }


    public Account getAccountByEmail(String email) {
        return accountRepository.findByEmail(email);
    }

}
