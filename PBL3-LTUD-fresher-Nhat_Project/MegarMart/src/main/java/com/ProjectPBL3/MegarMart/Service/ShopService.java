package com.ProjectPBL3.MegarMart.Service;

import com.ProjectPBL3.MegarMart.Entity.Account;
import com.ProjectPBL3.MegarMart.Entity.Role;
import com.ProjectPBL3.MegarMart.Entity.Shop;
import com.ProjectPBL3.MegarMart.Repository.RoleRepository;
import com.ProjectPBL3.MegarMart.Repository.ShopRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ShopService {
    private final ShopRepository shopRepository;
    private final RoleRepository roleRepository;
    private final FileSystemStorageService storageService;

    public Boolean save(Shop shop, MultipartFile file) {
        try {
            if (file != null && !file.isEmpty()) {
                // Nếu có upload ảnh mới thì cập nhật
                    storageService.store(file);
                    String filename = file.getOriginalFilename();
                    shop.setImageurl(filename);
            } else {
                shop.setImageurl("default-avatar-shop.jpg"); // Ảnh mặc định nếu không upload
            }
            shop.setStatus(false);
            shopRepository.save(shop);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public Shop findByAccount(Account account) {
        return shopRepository.findByAccount(account).orElse(null);
    }

    public List<Shop> getApproveShops() {return shopRepository.findByStatus(true);}

    public List<Shop> getPendingShops() {return shopRepository.findByStatus(false);}

    public void approveShop(int shopId){
        Shop shop = shopRepository.findById(shopId).orElseThrow(() -> new RuntimeException("Shop not found"));
        shop.setStatus(true);

        Account account = shop.getAccount();
        Role seller = roleRepository.findById(1).orElseThrow(() -> new RuntimeException("Shop not found"));
        account.setRole(seller);

        shopRepository.save(shop);
    }

    public void rejectShop(int shopId) {shopRepository.deleteById(shopId);}

    public Shop findById(int id) {return shopRepository.findById(id).get();}
}
