package com.ProjectPBL3.MegarMart.Controller;

import com.ProjectPBL3.MegarMart.Entity.*;
import com.ProjectPBL3.MegarMart.Service.*;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
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
import java.util.List;

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
        public String takemanager(HttpSession session,Model model){
            Account account = (Account) session.getAttribute("account");
            Shop shop = shopService.findByAccount(account);
            List<ReviewProduct> reviews = reviewProductService.getReviewsByShopId(shop.getId());
            model.addAttribute("reviews",reviews);
            model.addAttribute("id",shop.getId());
            return "Seller/seller_takecare";
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


    @GetMapping("/revenue")
    public String revenue(
            @RequestParam(value = "page", defaultValue = "1") Integer pageNo,
            Model model,
            HttpSession session) {
        Account account = (Account) session.getAttribute("account");
        Shop shop = shopService.findByAccount(account);

        List<Product> listpro = productService.findByShop(shop);
        List<OrderDetail> allOrderDetails = new ArrayList<>();
        int totalRevenue = 0;
        int thisMonthRevenue = 0;
        LocalDate now = LocalDate.now();
        for (Product product : listpro) {
            List<OrderDetail> details = orderDetailService.findOrderDetailByProduct(product);
            for (OrderDetail od : details) {
                int price = od.getPrice();
                totalRevenue += price;

                LocalDate createdAt = od.getOrder().getCreatedAt();
                if (createdAt.getMonthValue() == now.getMonthValue() && createdAt.getYear() == now.getYear()) {
                    thisMonthRevenue += price;
                }
            }
            allOrderDetails.addAll(details);
            allOrderDetails.sort((o1, o2) -> o2.getOrder().getCreatedAt().compareTo(o1.getOrder().getCreatedAt()));
        }

        // Phân trang thủ công (vì đang xài List chứ không phải Page trực tiếp)
        int pageSize = 5;
        int start = (pageNo - 1) * pageSize;
        int end = Math.min(start + pageSize, allOrderDetails.size());
        List<OrderDetail> pageContent = new ArrayList<>();
        if (start <= end) {
            pageContent = allOrderDetails.subList(start, end);
        }

        int totalPages = (int) Math.ceil((double) allOrderDetails.size() / pageSize);

        model.addAttribute("listOrder", pageContent);
        model.addAttribute("currentpage", pageNo);
        model.addAttribute("totalpage", totalPages);

        model.addAttribute("totalRevenue", totalRevenue);
        model.addAttribute("thisMonthRevenue", thisMonthRevenue);
        return "Seller/revenue";
    }

    @GetMapping("/revenue/filter")
    public String getfilterRevenue(
            @RequestParam(value = "keyword", required = false) String keyword,
            @RequestParam(value = "page", defaultValue = "1") Integer pageNo,
            @RequestParam(value = "fromDate", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fromDate,
            @RequestParam(value = "toDate", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate toDate,
            Model model, HttpSession session
    ) {
        Account account = (Account) session.getAttribute("account");
        Shop shop = shopService.findByAccount(account);

        List<Product> listpro = productService.findByShop(shop);
        List<OrderDetail> filteredDetails = new ArrayList<>();
        List<OrderDetail> allOrderDetails = new ArrayList<>();
        int total = 0;

        // Nếu có từ khóa tìm kiếm
        if (keyword != null && !keyword.trim().isEmpty()) {
            // Tìm sản phẩm theo từ khóa
            List<Product> searchProducts = productService.searchProductByNameAndShop(keyword, shop);
            for (Product product : searchProducts) {
                List<OrderDetail> details = orderDetailService.findOrderDetailByProduct(product);
                filteredDetails.addAll(details);
            }
        } else {
            // Không tìm kiếm -> lấy hết tất cả
            for (Product product : listpro) {
                List<OrderDetail> details = orderDetailService.findOrderDetailByProduct(product);
                filteredDetails.addAll(details);
            }
        }

        // Lọc theo khoảng thời gian nếu có chọn ngày
        for (OrderDetail od : filteredDetails) {
            LocalDate createdAt = od.getOrder().getCreatedAt();

            if ((fromDate == null || !createdAt.isBefore(fromDate)) &&
                    (toDate == null || !createdAt.isAfter(toDate))) {
                allOrderDetails.add(od);
                total += od.getPrice();
            }
        }

        // Sắp xếp theo ngày mới nhất
        allOrderDetails.sort((o1, o2) -> o2.getOrder().getCreatedAt().compareTo(o1.getOrder().getCreatedAt()));

        // Phân trang thủ công
        int pageSize = 5;
        int start = (pageNo - 1) * pageSize;
        int end = Math.min(start + pageSize, allOrderDetails.size());

        List<OrderDetail> pageContent = new ArrayList<>();
        if (start < end) {
            pageContent = allOrderDetails.subList(start, end);
        }

        int totalPages = (int) Math.ceil((double) allOrderDetails.size() / pageSize);

        model.addAttribute("fromDate", fromDate);
        model.addAttribute("toDate", toDate);
        model.addAttribute("listOrder", pageContent);
        model.addAttribute("currentpage", pageNo);
        model.addAttribute("totalpage", totalPages);
        model.addAttribute("keyword", keyword);
        model.addAttribute("total", total);

        return "Seller/RevenueFilter";
    }
    @PostMapping("/revenue/filter")
    public String filterRevenue(
            @RequestParam(value = "keyword", required = false) String keyword,
            @RequestParam(value = "page", defaultValue = "1") Integer pageNo,
            @RequestParam(value = "fromDate", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fromDate,
            @RequestParam(value = "toDate", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate toDate,
            Model model, HttpSession session
    ) {
        Account account = (Account) session.getAttribute("account");
        Shop shop = shopService.findByAccount(account);

        List<Product> listpro = productService.findByShop(shop);
        List<OrderDetail> filteredDetails = new ArrayList<>();
        List<OrderDetail> allOrderDetails = new ArrayList<>();
        int total = 0;

        // Nếu có từ khóa tìm kiếm
        if (keyword != null && !keyword.trim().isEmpty()) {
            // Tìm sản phẩm theo từ khóa
            List<Product> searchProducts = productService.searchProductByNameAndShop(keyword, shop);
            for (Product product : searchProducts) {
                List<OrderDetail> details = orderDetailService.findOrderDetailByProduct(product);
                filteredDetails.addAll(details);
            }
        } else {
            // Không tìm kiếm -> lấy hết tất cả
            for (Product product : listpro) {
                List<OrderDetail> details = orderDetailService.findOrderDetailByProduct(product);
                filteredDetails.addAll(details);
            }
        }

        // Lọc theo khoảng thời gian nếu có chọn ngày
        for (OrderDetail od : filteredDetails) {
            LocalDate createdAt = od.getOrder().getCreatedAt();

            if ((fromDate == null || !createdAt.isBefore(fromDate)) &&
                    (toDate == null || !createdAt.isAfter(toDate))) {
                allOrderDetails.add(od);
                total += od.getPrice();
            }
        }

        // Sắp xếp theo ngày mới nhất
        allOrderDetails.sort((o1, o2) -> o2.getOrder().getCreatedAt().compareTo(o1.getOrder().getCreatedAt()));

        // Phân trang thủ công
        int pageSize = 5;
        int start = (pageNo - 1) * pageSize;
        int end = Math.min(start + pageSize, allOrderDetails.size());

        List<OrderDetail> pageContent = new ArrayList<>();
        if (start < end) {
            pageContent = allOrderDetails.subList(start, end);
        }

        int totalPages = (int) Math.ceil((double) allOrderDetails.size() / pageSize);

        model.addAttribute("fromDate", fromDate);
        model.addAttribute("toDate", toDate);
        model.addAttribute("listOrder", pageContent);
        model.addAttribute("currentpage", pageNo);
        model.addAttribute("totalpage", totalPages);
        model.addAttribute("keyword", keyword);
        model.addAttribute("total", total);

        return "Seller/RevenueFilter";
    }


}
