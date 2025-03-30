package com.ProjectPBL3.MegarMart.Controller;

import com.ProjectPBL3.MegarMart.Entity.Shop;
import com.ProjectPBL3.MegarMart.Service.ShopService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/admin")
@RequiredArgsConstructor
public class AdminController {
    private final ShopService shopService;

    @GetMapping("/home")
    public String adminhome()
    {
        return "Admin/Home";
    }

    @GetMapping("/shop")
    public String adminshop(Model model)
    {
        model.addAttribute("listshop",shopService.getApproveShops());
        return "Admin/Shop";
    }

    @GetMapping("/addshop")
    public String adminaddshop(Model model)
    {
        List<Shop> pendingshop = shopService.getPendingShops();
        if(pendingshop.isEmpty()) {
            model.addAttribute("isempty", true);
        }
        else {
            model.addAttribute("pendingshop", pendingshop);
            model.addAttribute("isempty", false);
        }
        return "Admin/Add_Shop";
    }
    @PostMapping("/shop/approve/{id}")
    public String approveShop(@PathVariable int id) {shopService.approveShop(id); return "redirect:/admin/shop";}

    @PostMapping("/shop/reject/{id}")
    public String rejectShop(@PathVariable int id) {shopService.rejectShop(id); return"redirect:/admin/addshop";}
}
