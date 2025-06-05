package com.ProjectPBL3.MegarMart.SecurityConfig;

import com.ProjectPBL3.MegarMart.Entity.Account;
import com.ProjectPBL3.MegarMart.Service.AccountService;
import com.ProjectPBL3.MegarMart.Service.CartService;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.ui.Model;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.List;

@Component
public class OAuth2LoginSuccessHandler implements AuthenticationSuccessHandler {
    private final AccountService accountService;
    private final CartService cartService;

    public OAuth2LoginSuccessHandler(AccountService accountService, CartService cartService) {
        this.accountService = accountService;
        this.cartService = cartService;
    }


    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
                                    Authentication authentication) throws IOException, ServletException {
        OAuth2User oauthUser = (OAuth2User) authentication.getPrincipal();
        String email = oauthUser.getAttribute("email");
        Account account;
        if (accountService.checkExistedEmail(email)) {
            account = accountService.getAccountByEmail(email);
        }else{
        account = new Account();
        account.setEmail(email);
        account.setName(oauthUser.getAttribute("name"));
        account.setImageurl("default-avatar.jpg");

        accountService.create(account);
        }

        // Lưu account vào session
        request.getSession().setAttribute("account", account);

        // Tạo authentication mới với thông tin của Account
        Authentication newAuth = new UsernamePasswordAuthenticationToken(
            account, null, List.of(new SimpleGrantedAuthority(account.getRole().getRoleName()))
        );
        SecurityContextHolder.getContext().setAuthentication(newAuth);

        response.sendRedirect("/user/home");
        }

}
