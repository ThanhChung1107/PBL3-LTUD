package com.ProjectPBL3.MegarMart.Controller;

import com.ProjectPBL3.MegarMart.Entity.*;
import com.ProjectPBL3.MegarMart.Service.*;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.LinkedHashSet;


import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Set;
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

    @GetMapping("/home")
    public String userhome(Model model,HttpSession session)
    {
        session.setAttribute("listcart",cartService.getAllCartByAccount((Account) session.getAttribute("account")));
        model.addAttribute("listcate",categoryService.findAll());
        model.addAttribute("listpro",productService.findByStatus(1));
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
    public String userdetail() {return "User/account1";}



    @PostMapping("/accountdetail")
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
    public String pay(@RequestParam List<Integer> selectedIds,@RequestParam List<Integer> quantities,RedirectAttributes redirectAttributes ,Model model, HttpSession session) {
        Account acc = (Account) session.getAttribute("account");
        if (acc.getName() == null || acc.getName().isEmpty() ||
                acc.getPhone() == null || acc.getPhone().isEmpty() ||
                acc.getAddress() == null || acc.getAddress().isEmpty()) {
            redirectAttributes.addFlashAttribute("fillfull","Vui lòng điền đẩy đủ thông tin để mua hàng");
            return "redirect:/user/accountdetail";
        }

        List<Product> selectedProducts = cartService.getProductsInCartByIds(acc, selectedIds);

        int totalPrice = 0;
        for (int i = 0; i < selectedProducts.size(); i++) {
            Product product = selectedProducts.get(i);
            int quantity = quantities.get(i);
            totalPrice += product.getPrice() * quantity;
        }

        model.addAttribute("selectedProducts", selectedProducts);
        model.addAttribute("quantities", quantities);
        model.addAttribute("totalPrice", totalPrice);
        return "User/pay";
    }
}
