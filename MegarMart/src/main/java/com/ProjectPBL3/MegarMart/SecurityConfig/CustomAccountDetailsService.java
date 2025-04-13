package com.ProjectPBL3.MegarMart.SecurityConfig;

import com.ProjectPBL3.MegarMart.Entity.Account;
import com.ProjectPBL3.MegarMart.Repository.AccountRepository;
import com.ProjectPBL3.MegarMart.Repository.CartRepository;
import com.ProjectPBL3.MegarMart.Service.CartService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CustomAccountDetailsService implements UserDetailsService {
    private final AccountRepository accountRepository;
    private final CartService cartService;
    private final HttpSession session;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Account account = accountRepository.findByUsername(username);
        session.setAttribute("account",account);
        return new org.springframework.security.core.userdetails.User(
                account.getUsername(),
                account.getPassword(),
                List.of(new SimpleGrantedAuthority(account.getRole().getRoleName()))
        );
    }
}
