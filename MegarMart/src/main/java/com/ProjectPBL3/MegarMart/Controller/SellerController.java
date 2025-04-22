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

    @GetMapping("/takecare")
    public String takecare(){
        return "Seller/takecare";
    }

    @GetMapping("/revenue")
    public String revenue(Model model, HttpSession session) {
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


        model.addAttribute("totalRevenue", totalRevenue);
        model.addAttribute("thisMonthRevenue", thisMonthRevenue);
        model.addAttribute("listOrder", allOrderDetails); // Gửi lại danh sách sản phẩm cho view
        return "Seller/revenue";
    }
    @PostMapping("/revenue/filter")
    public String filterRevenue (
            @RequestParam("fromDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fromDate,
            @RequestParam("toDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate toDate,
            Model model, HttpSession session
    ){
        Account account = (Account) session.getAttribute("account");
        Shop shop = shopService.findByAccount(account);
        List<Product> listpro = productService.findByShop(shop);
        List<OrderDetail> filteredDetails = new ArrayList<>();
        int total = 0;

        for (Product p : listpro) {
            List<OrderDetail> details = orderDetailService.findOrderDetailByProduct(p);
            for (OrderDetail od : details) {
                LocalDate createdAt = od.getOrder().getCreatedAt();
                if ((createdAt.isEqual(fromDate) || createdAt.isAfter(fromDate)) &&
                        (createdAt.isEqual(toDate) || createdAt.isBefore(toDate))) {
                    filteredDetails.add(od);
                    total += od.getPrice();
                }
            }
        }

        filteredDetails.sort((o1, o2) -> o2.getOrder().getCreatedAt().compareTo(o1.getOrder().getCreatedAt()));

        model.addAttribute("listOrder", filteredDetails);
        model.addAttribute("total", total);
        return "Seller/RevenueFilter";
    }
    @GetMapping("/seller_profile")
    public String seller_profile(){
        return "Seller/seller_profile";
    }

}
