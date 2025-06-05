package com.ProjectPBL3.MegarMart.Controller;

import com.ProjectPBL3.MegarMart.Entity.*;
import com.ProjectPBL3.MegarMart.Service.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.*;
import java.util.stream.Collectors;


@Controller
@RequestMapping("/admin")
@RequiredArgsConstructor
public class AdminController {
    private final ShopService shopService;
    private final AccountService accountService;
    private final CategoryService categoryService;
    private final FileSystemStorageService storageService;
    private final ProductService productService;
    private final ReviewProductService reviewProductService;
    private final CouponService couponService;
    private final OrdersService ordersService;
    private final OrderDetailService orderDetailService;

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

    @PostMapping("/account/delete/{id}")
    public String deleteAccount(@PathVariable int id, RedirectAttributes redirectAttributes) {
        try {
            accountService.deleteById(id);
            redirectAttributes.addFlashAttribute("success", "Xóa tài khoản thành công.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Xóa tài khoản thất bại: " + e.getMessage());
        }
        return "redirect:/admin/home"; // Hoặc trang danh sách account nếu bạn có trang riêng
    }

    @GetMapping("/shop/stats/{id}")
    public String shopStats(@PathVariable Integer id, Model model) {
        Shop shop = shopService.findById(id);
        model.addAttribute("shopname", shop.getShopname());


        double totalRevenue = orderDetailService.getTotalRevenueByShop(id);
        model.addAttribute("monthlyRevenue", totalRevenue);


        int totalOrders = orderDetailService.countDistinctOrdersByShop(id);
        model.addAttribute("monthlyOrders", totalOrders);


        int productCount = productService.countByShopId(id);
        model.addAttribute("productCount", productCount);


        Double averageRating = reviewProductService.getAverageRatingByShop(id);
        model.addAttribute("averageRating", averageRating != null ?
                Math.round(averageRating * 10) / 10.0 : null);


        List<ReviewProduct> reviews = reviewProductService.getRecentReviewsByShop(id, 5);
        model.addAttribute("reviews", reviews);


        Map<String, Double> monthlyRevenue = orderDetailService.getMonthlyRevenueByShop(id, 12);
        model.addAttribute("monthLabels", monthlyRevenue.keySet());
        model.addAttribute("revenueData", monthlyRevenue.values());

        return "Admin/stats_shop";
    }

//        // (Optional) Nếu có thêm top sản phẩm thì cũng ném vào luôn
//        List<String> topProductNames = productService.getTopSellingProductNamesByShop(id, 5);
//        List<Integer> topProductSales = productService.getTopSellingProductSalesByShop(id, 5);
//        model.addAttribute("topProductNames", topProductNames);
//        model.addAttribute("topProductSales", topProductSales);

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
        List<Product> all = productService.findAll();
        List<Product> pending = all.stream()
                .filter(p -> p.getStatus() == 0)
                .collect(Collectors.toList());
        model.addAttribute("listpro", pending);
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

    @GetMapping("/order-history/{id}")
    public String orderHistory(@PathVariable int id, Model model) {
        Account acc = accountService.findById(id);
        List<Orders> orders = ordersService.findByAccount(acc);

        model.addAttribute("account", acc);
        model.addAttribute("orders", orders);
        return "Admin/OrderHistory";
    }

}