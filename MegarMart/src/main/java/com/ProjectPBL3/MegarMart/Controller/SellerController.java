package com.ProjectPBL3.MegarMart.Controller;

import com.ProjectPBL3.MegarMart.Entity.*;
import com.ProjectPBL3.MegarMart.Service.*;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.data.repository.query.Param;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/seller")
@RequiredArgsConstructor
public class SellerController {
    private final OrderDetailService orderDetailService;
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
    @GetMapping("/editproduct/{id}")
    public String editproduct(Model model, @PathVariable("id") Integer id) {
        Product pro = this.productService.findById(id);
//        List<Category> listcate = categoryService.findAll();

        model.addAttribute("product", pro);
//        model.addAttribute("listcate", listcate);
        return "Seller/update_product";
    }

    @PostMapping("/editproduct/{id}")
    public String editproduct(@ModelAttribute Product product, HttpSession session) {
        Account account = (Account) session.getAttribute("account");
        Shop shop = shopService.findByAccount(account);

        Product oldProduct = productService.findById(product.getId());

        // Giữ lại những trường không được sửa
        product.setName(oldProduct.getName());
        product.setImageurl(oldProduct.getImageurl());
        product.setCategory(oldProduct.getCategory());
        product.setRevenue(oldProduct.getRevenue());
        product.setSold(oldProduct.getSold());
        product.setShop(shop);
        product.setStatus(oldProduct.getStatus());

        productService.update(product); // Không cần file nữa
        return "redirect:/seller/product-manager";
    }


    @GetMapping("/deleteproduct/{id}")
    public String deleteProduct(@PathVariable("id") Integer id, RedirectAttributes redirectAttributes) {
        boolean isDelete = productService.delete(id);

        if (isDelete) {
            redirectAttributes.addFlashAttribute("deleteSuccess", true);
        } else {
            redirectAttributes.addFlashAttribute("deleteFail", true);
        }
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
        public String takemanager(    @RequestParam(value = "page", defaultValue = "1") Integer pageNo,
                                      HttpSession session,
                                      Model model){
            Account account = (Account) session.getAttribute("account");
            Shop shop = shopService.findByAccount(account);

            int pageSize = 5;
            Pageable pageable = PageRequest.of(pageNo - 1, pageSize);
            Page<ReviewProduct> page = reviewProductService.getReviewsByShopId(shop.getId(), pageable);
            List<ReviewProduct> reviews = page.getContent();
            List<ReviewProduct> reviewCount = reviewProductService.getReviewsByShopId(shop.getId());
            int totalReviews = reviewCount.size();
            int totalPages = page.getTotalPages();

            if (totalPages == 0) {
                totalPages = 1; // Không có đánh giá nào, tránh chia trang bị lỗi
            }

            model.addAttribute("currentpage", pageNo);
            model.addAttribute("totalpage", totalPages);
            model.addAttribute("totalReviews", totalReviews);
            model.addAttribute("reviews",reviews);
            model.addAttribute("id",shop.getId());
            return "Seller/seller_takecare";
        }
    @GetMapping("/filter")
    public String filterReviews(
            @RequestParam(required = false) RatingLevel rating,
            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate startDate,
            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate endDate,
            @RequestParam(value = "keyword", required = false) String keyword,
            @RequestParam(value = "page", defaultValue = "1") Integer pageNo,
            HttpSession session,
            Model model) {

        Account account = (Account) session.getAttribute("account");
        Shop shop = shopService.findByAccount(account);

        // Lọc review có keyword
        List<ReviewProduct> filteredReviews = reviewProductService.filterReviews(shop.getId(), rating, startDate, endDate, keyword);

        int totalReviews = filteredReviews.size();

        // Phân trang
        int pageSize = 5;
        int start = (pageNo - 1) * pageSize;
        int end = Math.min(start + pageSize, totalReviews);

        List<ReviewProduct> pageReviews = new ArrayList<>();
        if (start < end) {
            pageReviews = filteredReviews.subList(start, end);
        }

        int totalPages = (int) Math.ceil((double) totalReviews / pageSize);

        if (totalPages == 0) {
            totalPages = 1; // Không có đánh giá nào, tránh chia trang bị lỗi
        }


        model.addAttribute("reviews", pageReviews);
        model.addAttribute("totalReviews", totalReviews);
        model.addAttribute("ratingFilter", rating);
        model.addAttribute("startDate", startDate);
        model.addAttribute("endDate", endDate);
        model.addAttribute("keyword", keyword);
        model.addAttribute("currentpage", pageNo);
        model.addAttribute("totalpage", totalPages);

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

        return "redirect:/seller/takecare-manager"; // Quay lại trang quản lý đánh giá
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


    @GetMapping("/revenue")
    public String revenue(
            @RequestParam(value = "page", defaultValue = "1") Integer pageNo,
            Model model,
            HttpSession session) {

        Account account = (Account) session.getAttribute("account");
        Shop shop = shopService.findByAccount(account);

        int pageSize = 5;
        Pageable pageable = PageRequest.of(pageNo - 1, pageSize,
                Sort.by(Sort.Direction.DESC, "order.createdAt"));

        // Chỉ lấy OrderDetail của shop đã thanh toán
        Page<OrderDetail> page = orderDetailService
                .findOrderDetailsByShop(shop, pageable);
        List<OrderDetail> orderDetails = page.getContent();

        // Lấy toàn bộ để tính tổng (không phân trang)
        List<OrderDetail> allOrderDetails = orderDetailService
                .findOrderDetailsByShop(shop);

        // Tính tổng doanh thu và doanh thu tháng này
        int totalRevenue = allOrderDetails.stream()
                .mapToInt(OrderDetail::getPrice)
                .sum();

        LocalDate now = LocalDate.now();
        int thisMonthRevenue = allOrderDetails.stream()
                .filter(od -> {
                    LocalDate d = od.getOrder().getCreatedAt();
                    return d.getYear() == now.getYear()
                            && d.getMonthValue() == now.getMonthValue();
                })
                .mapToInt(OrderDetail::getPrice)
                .sum();

        int totalPages = page.getTotalPages();
        if (totalPages == 0) totalPages = 1;

        model.addAttribute("listOrder", orderDetails);
        model.addAttribute("currentpage", pageNo);
        model.addAttribute("totalpage", totalPages);
        model.addAttribute("totalRevenue", totalRevenue);
        model.addAttribute("thisMonthRevenue", thisMonthRevenue);

        return "Seller/revenue";
    }

    @GetMapping("/revenue/filter")
    public String filterRevenue(
            @RequestParam(value = "keyword", required = false) String keyword,
            @RequestParam(value = "fromDate", required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fromDate,
            @RequestParam(value = "toDate", required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate toDate,
            @RequestParam(value = "page", defaultValue = "1") Integer pageNo,
            Model model,
            HttpSession session) {

        Account account = (Account) session.getAttribute("account");
        Shop shop = shopService.findByAccount(account);

        // Lấy toàn bộ đã filter & paid
        List<OrderDetail> allFiltered = orderDetailService
                .findFilteredOrderDetails(shop, keyword, fromDate, toDate);

        int totalRevenue = allFiltered.stream()
                .mapToInt(OrderDetail::getPrice)
                .sum();

        // Lấy page đã filter & paid
        Pageable pageable = PageRequest.of(pageNo - 1, 5,
                Sort.by(Sort.Direction.DESC, "order.createdAt"));
        Page<OrderDetail> page = orderDetailService
                .findFilteredOrderDetailsPage(shop, keyword, fromDate, toDate, pageable);
        List<OrderDetail> pageContent = page.getContent();

        int totalPages = page.getTotalPages();
        if (totalPages == 0) totalPages = 1;

        model.addAttribute("fromDate", fromDate);
        model.addAttribute("toDate", toDate);
        model.addAttribute("keyword", keyword);
        model.addAttribute("listOrder", pageContent);
        model.addAttribute("currentpage", pageNo);
        model.addAttribute("totalpage", totalPages);
        model.addAttribute("total", totalRevenue);

        return "Seller/RevenueFilter";
    }

}
