package com.ProjectPBL3.MegarMart.Controller;

import com.ProjectPBL3.MegarMart.Entity.Category;
import com.ProjectPBL3.MegarMart.Entity.Shop;
import com.ProjectPBL3.MegarMart.Service.AccountService;
import com.ProjectPBL3.MegarMart.Service.CategoryService;
import com.ProjectPBL3.MegarMart.Service.FileSystemStorageService;
import com.ProjectPBL3.MegarMart.Service.ShopService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Controller
@RequestMapping("/admin")
@RequiredArgsConstructor
public class AdminController {
    private final ShopService shopService;
    private final AccountService accountService;
    private final CategoryService categoryService;
    private final FileSystemStorageService storageService;

    @GetMapping("/home")
    public String adminhome(Model model)
    {
        model.addAttribute("listacc",accountService.findAll());
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

    @GetMapping("/addcategory")
    public String addcategory(Model model){
        model.addAttribute("category",new Category());
        return "Admin/add_category";
    }

    @PostMapping("/addcategory")
    public String addcategoryy(@ModelAttribute Category category,@RequestParam(value = "fileImage", required = false) MultipartFile file){
        if(categoryService.create(category,file)){
            return "redirect:/admin/category";
        }
        else return "Admin/add_category";
    }

    @GetMapping("/category")
    public String category(Model model){
        model.addAttribute("listcate",categoryService.findAll());
        return "Admin/category";
    }

    @GetMapping("/category/edit/{id}")
    public String editcategory(Model model,@PathVariable Integer id){
        Category category = categoryService.findById(id);
        model.addAttribute("category",category);
        return "Admin/edit_category";
    }

    @PostMapping("/editcategory")
    public String editt(@ModelAttribute Category category,@RequestParam(value = "fileImage",required = false) MultipartFile file){
        if(file!=null && !file.isEmpty()){
            storageService.store(file);
            String filename = file.getOriginalFilename();
            category.setImageurl(filename);
        }
        else{
            Category oldcate = categoryService.findById(category.getId());
            category.setImageurl(oldcate.getImageurl());
        }
        if(categoryService.update(category)) return "redirect:/admin/category";
        else return "redirect:/admin/category";
    }

    @PostMapping("/category/delete/{id}")
    public String deletecategory(@PathVariable int id) {categoryService.deleteById(id);return "redirect:/admin/category";}
}
