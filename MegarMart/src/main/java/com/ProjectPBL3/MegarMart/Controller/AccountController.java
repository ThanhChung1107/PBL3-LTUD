package com.ProjectPBL3.MegarMart.Controller;

import com.ProjectPBL3.MegarMart.Entity.Account;
import com.ProjectPBL3.MegarMart.Service.AccountService;
import com.ProjectPBL3.MegarMart.Service.FileSystemStorageService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;

@Controller
@RequiredArgsConstructor
public class AccountController {
    private final AccountService accountService;

    private final FileSystemStorageService storageService;

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

    @GetMapping("/user/register")
    public String userregis() {return "User/seller_registration";}

    @GetMapping("/user/accountdetail")
    public String userdetail() {return "User/account1";}



    @PostMapping("/user/accountdetail")
    public String userUpdate(@RequestParam("id") Integer id,
                             @RequestParam("name") String name,
                             @RequestParam("address") String address,
                             @RequestParam("phone") String phone,
                             @RequestParam("fileImage") MultipartFile file,
                             HttpSession session) {
        // Tìm tài khoản cũ trong database
        Account existingAccount = accountService.findById(id);

        if (existingAccount != null) {
            existingAccount.setName(name);
            existingAccount.setAddress(address);
            existingAccount.setPhone(phone);
            existingAccount.setUpdatedAt(LocalDate.now());

            // Nếu có upload ảnh mới thì cập nhật
            if (file != null && !file.isEmpty()) {
                storageService.store(file);
                String filename = file.getOriginalFilename();
                existingAccount.setImageurl(filename);
            }

            // Lưu vào database
            accountService.update(existingAccount);
            session.setAttribute("account",existingAccount);
        }

        return "redirect:/user/accountdetail";
    }

}
