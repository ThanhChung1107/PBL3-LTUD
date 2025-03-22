package com.ProjectPBL3.MegarMart.SecurityConfig;

import com.ProjectPBL3.MegarMart.Entity.Account;
import com.ProjectPBL3.MegarMart.Service.AccountService;
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

import java.io.IOException;
import java.util.List;

@Component
public class OAuth2LoginSuccessHandler implements AuthenticationSuccessHandler {
    private final AccountService accountService;

    public OAuth2LoginSuccessHandler(AccountService accountService) {
        this.accountService = accountService;
    }


@Override
public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
                                    Authentication authentication) throws IOException, ServletException {
    OAuth2User oauthUser = (OAuth2User) authentication.getPrincipal();
    String email = oauthUser.getAttribute("email");
    if (accountService.checkExistedEmail(email)) {
            String role = accountService.getAccountByEmail(email).getRole().getRoleName();
            Authentication newAuth = new UsernamePasswordAuthenticationToken(
                    oauthUser, null, List.of(new SimpleGrantedAuthority(role))
            );
            SecurityContextHolder.getContext().setAuthentication(newAuth);
        response.sendRedirect("/user/home");
    } else {
//        request.getSession().setAttribute("ggImage", oauthUser.getAttribute("picture"));
//        request.getSession().setAttribute("ggEmail", email);
//        request.getSession().setAttribute("ggName", oauthUser.getAttribute("name"));

        Account account = new Account();
        account.setEmail(email);
        account.setName(oauthUser.getAttribute("name"));
        account.setImageurl(oauthUser.getAttribute("picture"));

        accountService.create(account);
        Authentication newAuth = new UsernamePasswordAuthenticationToken(
          oauthUser,null,List.of(new SimpleGrantedAuthority(account.getRole().getRoleName()))
        );
        SecurityContextHolder.getContext().setAuthentication(newAuth);
        response.sendRedirect("/user/home");
    }
}
}
