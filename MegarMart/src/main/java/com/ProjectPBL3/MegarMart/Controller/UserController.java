package com.ProjectPBL3.MegarMart.Controller;

import com.ProjectPBL3.MegarMart.Entity.*;
import com.ProjectPBL3.MegarMart.PaymentConfig.VNPAYService;
import com.ProjectPBL3.MegarMart.Service.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.nio.file.AccessDeniedException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;


import java.time.LocalDate;
import java.util.stream.Collectors;

@Controller
@RequiredArgsConstructor
@RequestMapping("/user")
public class UserController {
    private final ShopService shopService;
    private final CategoryService categoryService;
    private final AccountService accountService;
    private final FileSystemStorageService storageService;
    private final ProductService productService;
    private final CartService cartService;
    private final CouponService couponService;
    private final OrdersService ordersService;
    private final ReviewProductService reviewProductService;
    private final OrderDetailService orderDetailService;

    private final VNPAYService vnpayService;
    private final EmailService emailService;

    @GetMapping("/home")
    public String userhome(Model model, HttpSession session, @Param("keyword") String keyword, @RequestParam(value = "page", defaultValue = "1") Integer page)
    {
        Page<Product> list = productService.getAll(page);

        if(keyword != null) {
            list = productService.searchProduct(keyword,page);
            model.addAttribute("keyword", keyword);
        }

        model.addAttribute("totalpage", list.getTotalPages());
        model.addAttribute("currentpage", page);
        model.addAttribute("list", list);

        session.setAttribute("listcart",cartService.getAllCartByAccount((Account) session.getAttribute("account")));
        model.addAttribute("listcate",categoryService.findAll());
        if (keyword == null || keyword.isEmpty()) {
            model.addAttribute("listpro", productService.findByStatus(1));
        }
        return "User/Home";
    }

    @GetMapping("/home/{id}")
    public String userhomecate(@PathVariable int id,
                               @Param("keyword") String keyword,
                               @RequestParam(value = "page", defaultValue = "1") Integer page,
                               Model model,
                               HttpSession session) {

        session.setAttribute("listcart", cartService.getAllCartByAccount((Account) session.getAttribute("account")));
        model.addAttribute("listcate", categoryService.findAll());

        Category category = categoryService.findById(id);
        model.addAttribute("category", category);

        Page<Product> list;

        if (keyword != null && !keyword.isEmpty()) {
            list = productService.searchProductByCategory(keyword, category, page); // <-- xử lý tìm kiếm
            model.addAttribute("keyword", keyword);
        } else {
            list = productService.findByStatusAndCategory(1, category, page);
        }

        model.addAttribute("listprocate", list.getContent());
        model.addAttribute("totalpage", list.getTotalPages());
        model.addAttribute("currentpage", page);

        return "User/Home";
    }


    @GetMapping("/register")
    public String showRegisterPage(Model model, HttpSession session) {
        Account account = (Account) session.getAttribute("account");
        Shop shop = shopService.findByAccount(account);
        if (shop != null && !shop.getStatus()) {
            model.addAttribute("pending", true); // Đánh dấu trạng thái đang chờ duyệt
        } else {
            model.addAttribute("pending", false);
            if (shop == null) {
                shop = new Shop(); // Tạo Shop mới nếu chưa đăng ký
            }
            model.addAttribute("shop", shop);
        }
        model.addAttribute("account",account);
        return "User/seller_registration";
    }

    @GetMapping("/accountdetail")
    public String userdetail(Model model,HttpSession session) {
        model.addAttribute("orders",ordersService.findByAccount((Account) session.getAttribute("account") ));
        return "User/account1";
    }



    @PostMapping("/accountdetail")
    public String userUpdate(@RequestParam("id") Integer id,
                             @RequestParam("name") String name,
                             @RequestParam("address") String address,
                             @RequestParam("phone") String phone,
                             @RequestParam("fileImage") MultipartFile file,
                             Model model,
                             HttpSession session) {
        // Tìm tài khoản cũ trong database
        Account existingAccount = accountService.findById(id);
        if (existingAccount != null ) {
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
            accountService.update(existingAccount);
            session.setAttribute("account",existingAccount);
        }

        return "redirect:/user/accountdetail";
    }

    @PostMapping("/register")
    public String register(@ModelAttribute Shop shop,
                           @RequestParam("account_id") Integer accountId,
                           @RequestParam(value = "fileImage", required = false) MultipartFile file) {
        Account account = accountService.findById(accountId);
        shop.setAccount(account);
        shopService.save(shop, file);  // Gửi cả file ảnh
        return "redirect:/user/home";
    }

    @GetMapping("/productdetail/{id}")
    public String productdetail(@PathVariable int id,Model model){
        Product product = productService.findById(id);
        model.addAttribute("pro",product);
        List<ReviewProduct> reviews = this.reviewProductService.findByProductId(id);
        model.addAttribute("product",product);
        model.addAttribute("listreview",reviews);
        model.addAttribute("reviewCount",reviews.size());
        int productCount = productService.countByShopId(product.getShop().getId());
        model.addAttribute("productcount", productCount);
        return "User/productdetail";
    }

    @GetMapping("/shop/{id}")
    public String shopindex(@PathVariable int id,Model model){
        Shop shop= shopService.findById(id);
        model.addAttribute("shop",shop);

        int productCount = productService.countByShopId(id);
        model.addAttribute("productcount", productCount);
        List<ReviewProduct> reviewCount = reviewProductService.getReviewsByShopId(shop.getId());
        int totalReviews = reviewCount.size();
        model.addAttribute("totalReviews", totalReviews);

        List<Product> productshop = productService.findByShopAndStatus(shop);
        model.addAttribute("listproshop",productshop);

        Set<Category> uniqueCategories = productshop.stream()
                .map(Product::getCategory)
                .collect(Collectors.toCollection(LinkedHashSet::new)); // giữ thứ tự xuất hiện

        model.addAttribute("cateshop", uniqueCategories);

        return "User/shopindex";
    }


    @PostMapping("/addcart/{id}")
    public String addcart(@PathVariable int id,HttpSession session){
        Account account = (Account) session.getAttribute("account");
        Product product = productService.findById(id);
        cartService.addProductToCart(account,product);
        session.setAttribute("listcart",cartService.getAllCartByAccount(account));
        return "redirect:/user/productdetail/" + id;
    }

    @PostMapping("/deletecart/{id}")
    public String deletecart(@PathVariable int id,HttpSession session){
        Account acc = (Account) session.getAttribute("account");
        cartService.deleteProductFromCart(acc,id);
        return "redirect:/user/cart";
    }

    @GetMapping("/cart")
    public String cart(@RequestParam(name = "buynow", required = false) Integer buynowProductId,Model model,HttpSession session){
        Account acc = (Account) session.getAttribute("account");
        Cart cart = cartService.findByAccount(acc);// hàm này trả về Cart

        if (cart == null) {
            cart = new Cart();
            cart.setAccount(acc);
            cartService.save(cart); // hoặc cartService.createCartForAccount(acc)
        }


        // Nếu có tham số buynow
        if (buynowProductId != null) {
            Product product = productService.findById(buynowProductId);
            if (product != null && !cart.getProducts().contains(product)) {
                cartService.addProductToCart(acc, product);  // Bạn tự định nghĩa hàm này
            }
        }


        List<Product> productList = cart.getProducts();   // lấy sản phẩm từ giỏ hàng

        // Nhóm sản phẩm theo Shop
        Map<Shop, List<Product>> groupedCart = productList.stream()
                .collect(Collectors.groupingBy(Product::getShop));

        model.addAttribute("groupcart", groupedCart);

        // Truyền ID sản phẩm cần auto-tick (nếu có)
        if (buynowProductId != null) {
            model.addAttribute("buynowId", buynowProductId);
        }

        return "User/cart";
    }

    @GetMapping("/pay")
    public String pay(@RequestParam List<Integer> selectedIds,
                      @RequestParam List<Integer> quantities,
                      @RequestParam(required = false) String voucher,
                      RedirectAttributes redirectAttributes,
                      Model model,
                      HttpSession session) {

        Account acc = (Account) session.getAttribute("account");
        if (acc.getName() == null || acc.getName().isEmpty() ||
                acc.getPhone() == null || acc.getPhone().isEmpty() ||
                acc.getAddress() == null || acc.getAddress().isEmpty()) {
            redirectAttributes.addFlashAttribute("fillfull", "Vui lòng điền đầy đủ thông tin để mua hàng");
            return "redirect:/user/accountdetail";
        }

        List<Product> selectedProducts = cartService.getProductsInCartByIds(acc, selectedIds);

        int totalPrice = 0;
        for (int i = 0; i < selectedProducts.size(); i++) {
            Product product = selectedProducts.get(i);
            int quantity = quantities.get(i);
            totalPrice += product.getPrice() * quantity;
        }

        Coupon coupon = null;
        int coupondiscount = 0;
        if (voucher != null && !voucher.isEmpty()) {
            coupon = couponService.findByCode(voucher);
            if (coupon != null) {
                if (coupon.getStatus() == 0 || coupon.getCount() >= coupon.getUsagelimit()) {
                    model.addAttribute("error", "Mã không khả dụng hoặc đã hết lượt dùng!!");
                    coupon = null;
                } else {
                    coupondiscount = (int) ((coupon.getDiscount() / 100.0) * totalPrice);
                }
            } else {
                model.addAttribute("error", "Mã giảm giá không hợp lệ!");
            }
        }

        model.addAttribute("selectedProducts", selectedProducts);
        model.addAttribute("quantities", quantities);
        model.addAttribute("totalPrice", totalPrice);
        model.addAttribute("coupondiscount", coupondiscount);
        model.addAttribute("coupon", coupon);
        session.setAttribute("selectedProducts", selectedProducts);

        return "User/pay";
    }

    @PostMapping("/pay")
    public String placeOrder(@RequestParam("quantities") List<Integer> quantities,
                             @RequestParam("couponCode") String couponCode,
                             HttpSession session,
                             RedirectAttributes redirectAttributes,
                             Model model) {

        Account account = (Account) session.getAttribute("account");
        List<Product> selectedProducts = (List<Product>) session.getAttribute("selectedProducts");

        Orders order = new Orders();
        order.setAccount(account);
        order.setName(account.getName());
        order.setPhone(account.getPhone());
        order.setAddress(account.getAddress());

        List<OrderDetail> orderDetails = new ArrayList<>();
        int totalPrice = 0;

        for (int i = 0; i < selectedProducts.size(); i++) {
            Product product = selectedProducts.get(i);
            int quantity = quantities.get(i);

            if (product.getStock() < quantity) {
                model.addAttribute("errorproduct", "Sản phẩm " + product.getName() + " chỉ còn lại " + product.getStock() + " trong kho.");
                return "User/pay"; // Trả về lại trang pay nếu lỗi
            }

            OrderDetail detail = new OrderDetail();
            detail.setOrder(order);
            detail.setProduct(product);
            detail.setQuantity(quantity);
            detail.setPrice(product.getPrice() * quantity);
            orderDetails.add(detail);

            totalPrice += product.getPrice() * quantity;

            deletecart(product.getId(), session);
        }

        order.setOrderDetails(orderDetails);
        order.setIsPaid(0);

        if (couponCode != null && !couponCode.isEmpty()) {
            Coupon coupon = couponService.findByCode(couponCode);
            if (coupon != null) {
                order.setCoupon(coupon);
                coupon.setCount(coupon.getCount() + 1);
                couponService.save(coupon);
                totalPrice -= (int) ((coupon.getDiscount() / 100.0) * totalPrice);
            }
        }

        order.setTotalprice(totalPrice);
        ordersService.save(order); // Cascade OrderDetails tự động

        redirectAttributes.addAttribute("orderId", order.getId());
        redirectAttributes.addAttribute("amount", order.getTotalprice());
        return "redirect:/user/submitOrder";
    }

    @GetMapping("/submitOrder")
    public String submitOrder(@RequestParam("amount") int orderTotal,
                              @RequestParam("orderId") String orderInfo,
                              HttpServletRequest request) {

        String baseUrl = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort();
        String vnpayUrl = vnpayService.createOrder(request, orderTotal, orderInfo, baseUrl);
        return "redirect:" + vnpayUrl;
    }

    @GetMapping("/vnpay-payment-return")
    public String paymentCompleted(HttpServletRequest request, Model model) {
        int paymentStatus = vnpayService.orderReturn(request);

        String orderInfo = request.getParameter("vnp_OrderInfo");
        String paymentTime = request.getParameter("vnp_PayDate");
        DateTimeFormatter inputFormatter = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");
        LocalDateTime dateTime = LocalDateTime.parse(paymentTime, inputFormatter);

        String transactionId = request.getParameter("vnp_TransactionNo");
        String totalPrice = request.getParameter("vnp_Amount");

        Orders orders = ordersService.findById(Integer.parseInt(orderInfo));

        if (paymentStatus == 1) {
            ordersService.updateisPaid(orders); // Cập nhật isPaid = 1

            // ✅ Cập nhật kho và doanh thu
            for (OrderDetail detail : orders.getOrderDetails()) {
                Product product = detail.getProduct();
                int quantity = detail.getQuantity();

                product.setSold(product.getSold() + quantity);
                product.setStock(product.getStock() - quantity);
                product.setRevenue(product.getRevenue() + detail.getPrice());

                productService.update(product);
            }

            // Gửi email xác nhận
            Account account = orders.getAccount();
            String subject = "Đơn hàng #" + orderInfo + " đã được thanh toán thành công!";
            String content = "Chào " + account.getName() + ",\n\n"
                    + "Cảm ơn bạn đã đặt hàng tại cửa hàng của chúng tôi.\n"
                    + "Thông tin đơn hàng:\n"
                    + "- Mã đơn: " + orderInfo + "\n"
                    + "- Số tiền: " + (Integer.parseInt(totalPrice) / 100) + " VNĐ\n"
                    + "- Thời gian thanh toán: " + dateTime + "\n"
                    + "- Mã giao dịch: " + transactionId + "\n\n"
                    + "Chúng tôi sẽ sớm xử lý và giao hàng đến bạn.\n"
                    + "Trân trọng,\n"
                    + "Đội ngũ hỗ trợ khách hàng";

            emailService.sendEmail(account.getEmail(), subject, content);
        }

        model.addAttribute("orderId", orderInfo);
        model.addAttribute("totalPrice", Integer.parseInt(totalPrice) / 100);
        model.addAttribute("paymentTime", dateTime);
        model.addAttribute("transactionId", transactionId);

        return paymentStatus == 1 ? "Payment/orderSuccess" : "Payment/orderFail";
    }


    @GetMapping("/ReviewProduct-add")
    public String showReviewForm(
            @RequestParam("productId") Integer productId,
            HttpSession session,
            Model model) {

        // Kiểm tra đăng nhập
        Account account = (Account) session.getAttribute("account");
        if (account == null) return "redirect:/login";

        // Lấy thông tin sản phẩm
        Product product = productService.findById(productId);

        // Tạo đối tượng review mới
        ReviewProduct reviewProduct = new ReviewProduct();
        reviewProduct.setProduct(product);

        model.addAttribute("reviewProduct", reviewProduct);
        model.addAttribute("product", product);

        return "redirect:/user/accountdetail"; // Trả về trang account
    }

    @PostMapping("/ReviewProduct-add")
    public String submitReview(
            @ModelAttribute("reviewProduct") ReviewProduct reviewProduct,
            @RequestParam("productId") Integer productId,
            BindingResult result,
            HttpSession session) {

        // Xử lý lưu đánh giá
        Account account = (Account) session.getAttribute("account");
        Product product = productService.findById(productId);
        reviewProduct.setProduct(product);
        reviewProduct.setAccount(account);
        reviewProductService.save(reviewProduct);

        return "redirect:/user/accountdetail";
    }

}
