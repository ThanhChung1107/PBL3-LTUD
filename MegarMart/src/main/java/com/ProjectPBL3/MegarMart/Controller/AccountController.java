package com.ProjectPBL3.MegarMart.Controller;

import com.ProjectPBL3.MegarMart.Entity.Account;
import com.ProjectPBL3.MegarMart.Service.AccountService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequiredArgsConstructor
public class AccountController {
    private final AccountService accountService;

    @GetMapping("/login")
    public String login(@RequestParam(value = "error", required = false) String error, Model model) {
        if (error != null) {
            model.addAttribute("error", "Tên đăng nhập hoặc mật khẩu sai!!");
        }
        return "SignUp_SignIn/SignIn";
    }

    @GetMapping("/signup")
    public String signup(Model model)
    {
        model.addAttribute("account",new Account());
        return "SignUp_SignIn/SignUp";
    }

    @PostMapping("/signup")
    public String signUp(Model model, @ModelAttribute Account account) {
        if (accountService.checkExistedUsername(account.getUsername()))
        {
            model.addAttribute("error", "Tên đăng nhập đã tồn tại!!");
            model.addAttribute("account", account);
            return "SignUp_SignIn/SignUp";
        }
        else if(accountService.checkExistedEmail(account.getEmail()))
        {
            model.addAttribute("error", "Email đã tồn tại!!");
            model.addAttribute("account", account);
            return "SignUp_SignIn/SignUp";
        }
        else if(accountService.checkExistedPhone(account.getPhone()))
        {
            model.addAttribute("error", "Số điện thoại đã tồn tại!!");
            model.addAttribute("account", account);
            return "SignUp_SignIn/SignUp";
        }


        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        String encodedPassword = passwordEncoder.encode(account.getPassword());
        account.setPassword(encodedPassword);
        accountService.create(account);

        return "redirect:/login";
    }



//    @GetMapping("/createAdmin")
//    @ResponseBody
//    public String createAdmin() {
//        try {
//            accountService.createAdminAccount("admin", "admin", "ADMIN","admin@gmail.com");
//            return "Admin account created successfully!";
//        } catch (Exception e) {
//            return "Error: " + e.getMessage();
//        }
//    }
}
