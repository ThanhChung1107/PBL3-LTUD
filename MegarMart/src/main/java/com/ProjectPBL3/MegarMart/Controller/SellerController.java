package com.ProjectPBL3.MegarMart.Controller;

import com.ProjectPBL3.MegarMart.Entity.Account;
import com.ProjectPBL3.MegarMart.Entity.Category;
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
import java.util.List;

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
    @GetMapping("/editproduct/{id}")
    public String editproduct(Model model, @PathVariable("id") Integer id) {
        Product pro = this.productService.findById(id);
        List<Category> listcate = categoryService.findAll();

        model.addAttribute("product", pro);
        model.addAttribute("listcate", listcate); // <<<< cần cái này
        return "Seller/update_product";
    }

    @PostMapping("/editproduct/{id}")
    public String editproduct(@ModelAttribute Product product, HttpSession session) {
        Account account = (Account) session.getAttribute("account");
        Shop shop = shopService.findByAccount(account);

        Product oldProduct = productService.findById(product.getId());

        // Giữ lại những trường không được sửa
        product.setName(oldProduct.getName());
        product.setImageurl(oldProduct.getImageurl());// ✅ Giữ nguyên ảnh
        product.setCategory(oldProduct.getCategory());
        product.setRevenue(oldProduct.getRevenue());
        product.setSold(oldProduct.getSold());
        product.setShop(shop);
        product.setStatus(oldProduct.getStatus());

        productService.save(product); // Không cần file nữa
        return "redirect:/seller/product-manager";
    }

    @GetMapping("/deleteproduct/{id}")
    public String deleteproduct(Model model, @PathVariable("id") Integer id) {
      if(this.productService.delete(id)){
          return "redirect:/seller/product-manager";
      }else {
          return "redirect:/seller/product-manager";
      }
    }

    @GetMapping("/product-manager")
    public String productmanager(Model model,HttpSession session){
        Account account = (Account) session.getAttribute("account");
        Shop shop = shopService.findByAccount(account);
        model.addAttribute("listproshop",productService.findByShop(shop));
        return "Seller/product_manager";
    }

    @GetMapping("/takecare")
    public String takecare(){
        return "Seller/takecare";
    }

    @GetMapping("/revenue")
    public String revenue(){
        return "Seller/revenue";
    }


    @GetMapping("/seller_profile")
    public String seller_profile(){
        return "Seller/seller_profile";
    }

}
