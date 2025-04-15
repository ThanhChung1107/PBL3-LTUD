package com.ProjectPBL3.MegarMart.Controller;

import com.ProjectPBL3.MegarMart.Entity.Category;
import com.ProjectPBL3.MegarMart.Entity.Coupon;
import com.ProjectPBL3.MegarMart.Entity.Product;
import com.ProjectPBL3.MegarMart.Entity.Shop;
import com.ProjectPBL3.MegarMart.Service.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping("/admin")
@RequiredArgsConstructor
public class AdminController {
    private final ShopService shopService;
    private final AccountService accountService;
    private final CategoryService categoryService;
    private final FileSystemStorageService storageService;
    private final ProductService productService;
    private final CouponService couponService;

    @GetMapping("/home")
    public String adminhome(Model model) {
        model.addAttribute("listacc", accountService.findAll());
        return "Admin/Home";
    }

    @GetMapping("/shop")
    public String adminshop(Model model) {
        model.addAttribute("listshop", shopService.getApproveShops());
        return "Admin/Shop";
    }

    @GetMapping("/addshop")
    public String adminaddshop(Model model) {
        List<Shop> pendingshop = shopService.getPendingShops();
        if (pendingshop.isEmpty()) {
            model.addAttribute("isempty", true);
        } else {
            model.addAttribute("pendingshop", pendingshop);
            model.addAttribute("isempty", false);
        }
        return "Admin/Add_Shop";
    }

    @PostMapping("/shop/approve/{id}")
    public String approveShop(@PathVariable int id) {
        shopService.approveShop(id);
        return "redirect:/admin/shop";
    }

    @PostMapping("/shop/reject/{id}")
    public String rejectShop(@PathVariable int id) {
        shopService.rejectShop(id);
        return "redirect:/admin/addshop";
    }

    @GetMapping("/addcategory")
    public String addcategory(Model model) {
        model.addAttribute("category", new Category());
        return "Admin/add_category";
    }

    @PostMapping("/addcategory")
    public String addcategoryy(@ModelAttribute Category category, @RequestParam(value = "fileImage", required = false) MultipartFile file) {
        if (categoryService.create(category, file)) {
            return "redirect:/admin/category";
        } else return "Admin/add_category";
    }

    @GetMapping("/category")
    public String category(Model model) {
        model.addAttribute("listcate", categoryService.findAll());
        return "Admin/category";
    }

    @GetMapping("/category/edit/{id}")
    public String editcategory(Model model, @PathVariable Integer id) {
        Category category = categoryService.findById(id);
        model.addAttribute("category", category);
        return "Admin/edit_category";
    }

    @PostMapping("/editcategory")
    public String editt(@ModelAttribute Category category, @RequestParam(value = "fileImage", required = false) MultipartFile file) {
        if (file != null && !file.isEmpty()) {
            storageService.store(file);
            String filename = file.getOriginalFilename();
            category.setImageurl(filename);
        } else {
            Category oldcate = categoryService.findById(category.getId());
            category.setImageurl(oldcate.getImageurl());
        }
        if (categoryService.update(category)) return "redirect:/admin/category";
        else return "redirect:/admin/category";
    }

    @PostMapping("/category/delete/{id}")
    public String deletecategory(@PathVariable int id) {
        categoryService.deleteById(id);
        return "redirect:/admin/category";
    }

    @GetMapping("/product")
    public String product(Model model) {
        model.addAttribute("listpro",productService.findAll());
        return "Admin/product";
    }

    @PostMapping("/product/approve/{id}")
    public String approveProduct(@PathVariable int id) {
        productService.approveProduct(id);
        return "redirect:/admin/product";
    }

    @PostMapping("/product/reject/{id}")
    public String rejectProduct(@PathVariable int id) {
        productService.rejectProduct(id);
        return "redirect:/admin/product";
    }

    @GetMapping("/addcoupon")
    public String addcoupon(Model model){
        model.addAttribute("coupon",new Coupon());
        return "Admin/add_coupon";
    }

    @PostMapping("/addcoupon")
    public String addcou(@ModelAttribute Coupon coupon,Model model){
        if(couponService.save(coupon)) return "redirect:/admin/coupon";
        else {
            model.addAttribute("error", "CODE đã tồn tại");
            return "Admin/add_coupon";
        }
    }

    @GetMapping("/coupon")
    public String coupon(Model model){
        model.addAttribute("listcoupon",couponService.findAll());
        return "Admin/coupon";
    }


    @PostMapping("/coupon/toggle-status")
    public String toggleCouponStatus(@RequestParam("couponId") int couponId) {
        Coupon coupon = couponService.findById(couponId);
        if (coupon != null) {
            coupon.setStatus(coupon.getStatus() == 1 ? 0 : 1);
            if(couponService.save(coupon)) return "redirect:/admin/coupon";
        }
        return "redirect:/admin/coupon"; // hoặc trang hiện tại bạn muốn reload lại
    }

    @GetMapping("/edit-coupon/{id}")
    public String editcoupon(@PathVariable int id,Model model){
        model.addAttribute("coupon",couponService.findById(id));
        return "/Admin/edit_coupon";
    }

    @PostMapping("/editcoupon")
    public String editcouponn(@ModelAttribute Coupon coupon,Model model){
        if(couponService.update(coupon)) return "redirect:/admin/coupon";
        else {
            model.addAttribute("coupontontai","CODE đã tồn tại!");
            return "Admin/edit_coupon";
        }
    }
}