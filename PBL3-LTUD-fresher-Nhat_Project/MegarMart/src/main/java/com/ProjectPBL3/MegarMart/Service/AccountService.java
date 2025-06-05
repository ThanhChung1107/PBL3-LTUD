package com.ProjectPBL3.MegarMart.Service;

import com.ProjectPBL3.MegarMart.Entity.Account;
import com.ProjectPBL3.MegarMart.Entity.Role;
import com.ProjectPBL3.MegarMart.Repository.AccountRepository;
import com.ProjectPBL3.MegarMart.Repository.RoleRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.hibernate.Hibernate;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AccountService {
    private final AccountRepository accountRepository;
    private final RoleRepository roleRepository;

    public List<Account> findAll() {return accountRepository.findAll();}

    public void deleteById(int id) {
        // Nếu bạn cần kiểm tra tồn tại trước khi xóa thì có thể thêm:
        if (!accountRepository.existsById(id)) {
            throw new RuntimeException("Tài khoản không tồn tại");
        }
        accountRepository.deleteById(id);
    }

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
            account.setImageurl("default-avatar.jpg");
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

    public Account findById(Integer id) {return accountRepository.findById(id).get();}

    public void update(Account account) {
        accountRepository.updateAccountInfo(
                account.getId(),
                account.getName(),
                account.getAddress(),
                account.getPhone(),
                account.getUpdatedAt(),
                account.getImageurl()
        );
    }

    public Account findByEmail(String email) {
        return accountRepository.findByEmail(email);
    }


//    private final PasswordEncoder passwordEncoder;
//
//    public void createAdminAccount(String username, String password, String name,String email) {
//        if (accountRepository.findByUsername(username) != null) {
//            throw new RuntimeException("Admin username already exists!");
//        }
//
//        Role adminRole = roleRepository.findByRoleName("ROLE_ADMIN");
//        if (adminRole == null) {
//            throw new RuntimeException("Admin role not found! Please insert it into DB.");
//        }
//
//        Account admin = new Account();
//        admin.setUsername(username);
//        admin.setPassword(passwordEncoder.encode(password));
//        admin.setName(name);
//        admin.setEmail(email);
//        admin.setRole(adminRole);
//
//        accountRepository.save(admin);
//    }



}
