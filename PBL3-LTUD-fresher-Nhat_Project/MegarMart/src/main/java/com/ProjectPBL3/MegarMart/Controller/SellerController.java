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
import java.util.*;
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



    @GetMapping("/addproduct")
    public String addproduct(Model model){
        model.addAttribute("product",new Product());
        model.addAttribute("listcate",categoryService.findAll());
        model.addAttribute("pageTitle", "Thêm sản phẩm");
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
        model.addAttribute("pageTitle", "Sửa sản phẩm");
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
    public String productManager(
            @RequestParam(name = "page", defaultValue = "1") int page,
            @RequestParam(name = "keyword", required = false) String keyword,
            @RequestParam(name = "status", required = false) Integer status,
            Model model, HttpSession session) {

        Account account = (Account) session.getAttribute("account");
        Shop currentShop = shopService.findByAccount(account);
        int pageSize = 5;

        Page<Product> productPage;

        if (keyword != null && !keyword.isEmpty()) {
            if (status != null) {
                // Tìm kiếm theo tên + trạng thái
                productPage = productService.searchAndFilterProducts(currentShop, keyword, status, page, pageSize);
            } else {
                // Tìm kiếm theo tên
                productPage = productService.searchProductsByShopAndName(currentShop, keyword, page, pageSize);
            }
        } else {
            if (status != null) {
                // Lọc theo trạng thái
                productPage = productService.filterProductsByShopAndStatus(currentShop, status, page, pageSize);
            } else {
                // Lấy tất cả sản phẩm
                productPage = productService.getProductsByShop(currentShop, page, pageSize);
            }
        }
      int totalPages =productPage.getTotalPages();
        if (totalPages == 0) {
            totalPages = 1; // Không có đánh giá nào, tránh chia trang bị lỗi
        }

        model.addAttribute("listproshop", productPage.getContent());
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages",totalPages );
        model.addAttribute("keyword", keyword);
        model.addAttribute("statusFilter", status);

        // Thêm số lượng theo trạng thái để hiển thị trong filter
        model.addAttribute("activeCount", productService.countProductsByStatus(currentShop, 1));
        model.addAttribute("violateCount", productService.countProductsByStatus(currentShop, 2));
        model.addAttribute("pendingCount", productService.countProductsByStatus(currentShop, 0));
        model.addAttribute("pageTitle", "Quản lí sản phẩm");
        return "Seller/product_manager";
    }




    @GetMapping("/takecare-manager")
    public String takecareManager(
            @RequestParam(required = false) RatingLevel rating,
            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate startDate,
            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate endDate,
            @RequestParam(value = "keyword", required = false) String keyword,
            @RequestParam(value = "page", defaultValue = "1") Integer pageNo,
            HttpSession session,
            Model model) {

        Account account = (Account) session.getAttribute("account");
        Shop shop = shopService.findByAccount(account);

        int pageSize = 5;
        Pageable pageable = PageRequest.of(pageNo - 1, pageSize);

        Page<ReviewProduct> pageReviews;
        boolean hasFilter = (rating != null || startDate != null || endDate != null || (keyword != null && !keyword.trim().isEmpty()));

        if (hasFilter) {
            // Có filter => gọi hàm lọc
            pageReviews = reviewProductService.filterReviews(shop.getId(), rating, startDate, endDate, keyword, pageable);
        } else {
            // Không có filter => lấy tất cả
            pageReviews = reviewProductService.getReviewsByShopId(shop.getId(), pageable);
        }

        int totalPages = pageReviews.getTotalPages();
        if (totalPages == 0) totalPages = 1;

        int totalReviews = (int) pageReviews.getTotalElements();

        model.addAttribute("reviews", pageReviews.getContent());
        model.addAttribute("totalReviews", totalReviews);
        model.addAttribute("ratingFilter", rating);
        model.addAttribute("startDate", startDate);
        model.addAttribute("endDate", endDate);
        model.addAttribute("keyword", keyword);
        model.addAttribute("currentpage", pageNo);
        model.addAttribute("totalpage", totalPages);
        model.addAttribute("id", shop.getId());
        model.addAttribute("pageTitle", "Chăm sóc khách hàng");

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

        return "redirect:/Seller/takecare-manager"; // Quay lại trang quản lý đánh giá
    }



    @GetMapping("shopProfile")
    public String shopprofile(HttpSession session,Model model){
        Account account = (Account) session.getAttribute("account");
        Shop shop = shopService.findByAccount(account);
        model.addAttribute("shop",shop);
        model.addAttribute("pageTitle", "Quản lý Shop");
        return "Seller/seller_profile";
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
        return "redirect:/Seller/shopProfile";
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
                .mapToInt(od -> od.getPrice() * od.getQuantity())
                .sum();

        LocalDate now = LocalDate.now();
        int thisMonthRevenue = allOrderDetails.stream()
                .filter(od -> {
                    LocalDate d = od.getOrder().getCreatedAt();
                    return d.getYear() == now.getYear()
                            && d.getMonthValue() == now.getMonthValue();
                })
                .mapToInt(od -> od.getPrice() * od.getQuantity())
                .sum();

        Map<String, Integer> productSales = new HashMap<>();
        for (OrderDetail order : allOrderDetails) {
            String name = order.getProduct().getName();
            int qty = order.getQuantity();
            productSales.put(name, productSales.getOrDefault(name, 0) + qty);
        }

        model.addAttribute("productNames", productSales.keySet());
        model.addAttribute("productQuantities", productSales.values());
        int totalPages = page.getTotalPages();
        if (totalPages == 0) totalPages = 1;

        model.addAttribute("listOrder", orderDetails);
        model.addAttribute("currentpage", pageNo);
        model.addAttribute("totalpage", totalPages);
        model.addAttribute("totalRevenue", totalRevenue);
        model.addAttribute("thisMonthRevenue", thisMonthRevenue);
        model.addAttribute("pageTitle", "Tài chính");
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
                .mapToInt(od -> od.getPrice() * od.getQuantity())
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
        model.addAttribute("pageTitle", "Tài chính");
        return "Seller/RevenueFilter";
    }
   @GetMapping("/home")
public String homeOrFilter(
        @RequestParam(value = "page", defaultValue = "1") Integer pageNo,
        @RequestParam(value = "status", required = false) Integer status,
        @RequestParam(value = "keyword", required = false) String keyword,
        @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate startDate,
        @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate endDate,
        Model model, HttpSession session) {

    Account account = (Account) session.getAttribute("account");
    Shop shop = shopService.findByAccount(account);

    int pageSize = 5;
    Pageable pageable = PageRequest.of(pageNo - 1, pageSize);

    if (keyword != null && keyword.trim().isEmpty()) {
        keyword = null;
    }

    boolean hasFilter = (status != null || startDate != null || endDate != null || keyword != null);

    Page<OrderDetail> page;
    if (hasFilter) {
        // Gọi hàm lọc nếu có bất kỳ filter nào
        page = orderDetailService.findFiltered(shop, status, startDate, endDate, keyword, pageable);
    } else {
        // Không có filter → gọi findAll
        page = orderDetailService.findByShop(shop, pageable);
    }

    int totalPages = page.getTotalPages();
    if (totalPages == 0) totalPages = 1;

    int totalOrder = (int) page.getTotalElements();

    model.addAttribute("pageTitle", "Quản lý đơn hàng");
    model.addAttribute("orders", page.getContent());
    model.addAttribute("startDate", startDate);
    model.addAttribute("endDate", endDate);
    model.addAttribute("currentPage", pageNo);
    model.addAttribute("totalPages", totalPages);
    model.addAttribute("totalOrder", totalOrder);
    model.addAttribute("statusFilter", status);
    model.addAttribute("keyword", keyword);

    return "Seller/seller_home";
}




}
