package com.ProjectPBL3.MegarMart.SecurityConfig;

import com.ProjectPBL3.MegarMart.Entity.Account;
import com.ProjectPBL3.MegarMart.Repository.AccountRepository;
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


    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        String usernamee = "";
        String pw = "";
        String role = "";
        try {
            Account account = accountRepository.findByUsername(username);
            usernamee = account.getUsername();
            pw = account.getPassword();
            role = account.getRole().getRoleName();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return new org.springframework.security.core.userdetails.User(
                usernamee,
                pw,
                List.of(new SimpleGrantedAuthority(role))
        );
    }
}
