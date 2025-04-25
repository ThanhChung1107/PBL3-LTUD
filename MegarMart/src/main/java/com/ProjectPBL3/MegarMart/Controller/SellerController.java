package com.ProjectPBL3.MegarMart.Controller;

import com.ProjectPBL3.MegarMart.Entity.*;
import com.ProjectPBL3.MegarMart.Service.*;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Controller
@RequestMapping("/seller")
@RequiredArgsConstructor
public class SellerController {
    private final CategoryService categoryService;
    private final ShopService shopService;
    private final AccountService accountService;
    private final ProductService productService;
    private final ReviewProductService reviewProductService;

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

        @GetMapping("/takecare-manager")
        public String takemanager(HttpSession session,Model model){
            Account account = (Account) session.getAttribute("account");
            Shop shop = shopService.findByAccount(account);
            List<ReviewProduct> reviews = reviewProductService.getReviewsByShopId(shop.getId());
            model.addAttribute("reviews",reviews);
            model.addAttribute("id",shop.getId());
            return "seller/seller_takecare";
        }

    @PostMapping("/reply")
    public String reply(@RequestParam("reviewProductId") Integer reviewProductId,
                        @RequestParam("sellerReply") String sellerReply,
                        HttpSession session,
                        Model model) {
        Account seller = (Account) session.getAttribute("account");
        if (seller == null) {
            return "redirect:/login"; // nếu chưa đăng nhập thì chuyển hướng đến trang đăng nhập
        }

        // Tìm review cần phản hồi
        ReviewProduct review = reviewProductService.findById(reviewProductId);
        if (review != null) {
            review.setSellerReply(sellerReply); // Gán phản hồi
            reviewProductService.save(review);  // Lưu lại vào DB
        }

        Shop shop = shopService.findByAccount(seller);
        List<ReviewProduct> reviews = reviewProductService.getReviewsByShopId(shop.getId());
        model.addAttribute("reviews", reviews);
        model.addAttribute("id", shop.getId());

        return "redirect:seller/seller_takecare"; // Quay lại trang quản lý đánh giá
    }

    @GetMapping("/filter")
    public String filterReviews(
            @RequestParam(required = false) RatingLevel rating,
            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate startDate,
            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate endDate,
            HttpSession session,
            Model model) {

        Account account = (Account) session.getAttribute("account");
        Shop shop = shopService.findByAccount(account);

        List<ReviewProduct> reviews = reviewProductService.filterReviews(shop.getId(), rating, startDate, endDate);

        List<ReviewProduct> reviewCount = reviewProductService.getReviewsByShopId(shop.getId());
        int totalReviews = reviewCount.size();

        model.addAttribute("reviews", reviews);
        model.addAttribute("totalReviews", totalReviews);
        model.addAttribute("ratingFilter", rating);
        model.addAttribute("startDate", startDate);
        model.addAttribute("endDate", endDate);

        return "seller/seller_takecare";
    }

    @GetMapping("shopProfile")
    public String shopprofile(HttpSession session,Model model){
        Account account = (Account) session.getAttribute("account");
        Shop shop = shopService.findByAccount(account);
        model.addAttribute("shop",shop);
        return "seller/seller_profile";
    }

    @PostMapping("/edit_profile")
    public String editProfile(@RequestParam String name,
                              @RequestParam String description,
                              @RequestParam(required = false) MultipartFile imageFile,
                              HttpSession session){
        Account account = (Account) session.getAttribute("account");
        Shop shop = shopService.findByAccount(account);
        shop.setShopname(name);
        shop.setDescription(description);
        shopService.save(shop,imageFile);
        return "redirect:/seller/shopProfile";
    }
}
