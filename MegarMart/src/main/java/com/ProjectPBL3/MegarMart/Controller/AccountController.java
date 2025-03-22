package com.ProjectPBL3.MegarMart.Controller;

import com.ProjectPBL3.MegarMart.Entity.Account;
import com.ProjectPBL3.MegarMart.Service.AccountService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

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


//    @GetMapping("/signupGoogle")
//    public String signupGG(Model model, HttpServletRequest request)
//    {
//        String googleImage = (String) request.getSession().getAttribute("ggImage");
//        String googleEmail = (String) request.getSession().getAttribute("ggEmail");
//        String googleName = (String) request.getSession().getAttribute("ggName");
//        Account account = new Account();
//        account.setEmail(googleEmail);
//        account.setImageurl(googleImage);
//        model.addAttribute("account",account);
//        model.addAttribute("emaill",googleEmail);
//        model.addAttribute("image",googleImage);
//        model.addAttribute("name",googleName);
//        return "SignUp_SignIn/SignUpGG";
//    }
//
//    @PostMapping("/signupGoogle")
//    public String signupGGG(@ModelAttribute Account account, HttpServletRequest request, Model model) {
//        if (accountService.checkExistedPhone(account.getPhone())) {
//            model.addAttribute("error", "Số điện thoại đã tồn tại!");
//            model.addAttribute("account", account);
//            return "SignUp_SignIn/SignUpGG";
//        }
//
//        String googleImage = (String) request.getSession().getAttribute("ggImage");
//        String googleName = (String) request.getSession().getAttribute("ggName");
//        if (googleImage != null) {
//            account.setImageurl(googleImage);
//        }
//        account.setName(googleName);
//        accountService.create(account);
//        return "redirect:/user/home";
//    }


    @GetMapping("/admin/home")
    public String adminhome()
    {
        return "Admin/Home";
    }

    @GetMapping("/user/home")
    public String userhome()
    {
        return "User/Home";
    }

}
