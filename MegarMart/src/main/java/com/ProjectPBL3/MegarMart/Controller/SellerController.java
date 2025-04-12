package com.ProjectPBL3.MegarMart.Controller;

import com.ProjectPBL3.MegarMart.Entity.Account;
import com.ProjectPBL3.MegarMart.Entity.Product;
import com.ProjectPBL3.MegarMart.Entity.Shop;
import com.ProjectPBL3.MegarMart.Service.AccountService;
import com.ProjectPBL3.MegarMart.Service.CategoryService;
import com.ProjectPBL3.MegarMart.Service.ProductService;
import com.ProjectPBL3.MegarMart.Service.ShopService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;

@Controller
@RequestMapping("/seller")
@RequiredArgsConstructor
public class SellerController {
    private final CategoryService categoryService;
    private final ShopService shopService;
    private final AccountService accountService;
    private final ProductService productService;

    @GetMapping("/home")
    public String home(){
        return "Seller/seller_home";
    }

    @GetMapping("/addproduct")
    public String addproduct(Model model){
        model.addAttribute("product",new Product());
        model.addAttribute("listcate",categoryService.findAll());
        return "Seller/add_product";
    }
    @PostMapping("/addproduct")
    public String addproductt(@ModelAttribute Product product, @RequestParam("fileImage")MultipartFile file, HttpSession session){
        Account account = (Account) session.getAttribute("account");
        Shop shop = shopService.findByAccount(account);
        product.setShop(shop);
        product.setStatus(0);
        product.setRevenue(0);
        product.setSold(0);
        productService.save(product,file);
        return "redirect:/seller/product-manager";
    }

    @GetMapping("/product-manager")
    public String productmanager(Model model,HttpSession session){
        Account account = (Account) session.getAttribute("account");
        Shop shop = shopService.findByAccount(account);
        model.addAttribute("listproshop",productService.findByShop(shop));
        return "Seller/product_manager";
    }
}
