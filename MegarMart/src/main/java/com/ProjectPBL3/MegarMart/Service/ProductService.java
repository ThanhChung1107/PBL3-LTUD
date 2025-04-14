package com.ProjectPBL3.MegarMart.Service;

import com.ProjectPBL3.MegarMart.Entity.*;
import com.ProjectPBL3.MegarMart.Repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductService {
    private final CategoryService categoryService;
    private final FileSystemStorageService storageService;
    private final ProductRepository productRepository;

    public void save(Product product, MultipartFile file){
            storageService.store(file);
            String filename = file.getOriginalFilename();
            product.setImageurl(filename);
            productRepository.save(product);
    }

    public void update(Product product) {productRepository.save(product);}

    public List<Product> findAll() {return productRepository.findAll();}

    public List<Product> findByShop(Shop shop) {return productRepository.findByShop(shop);}

    public List<Product> findByShopAndStatus(Shop shop) {
        return productRepository.findByShopAndStatus(shop, 1); // status = 1
    }

    public List<Product> findByStatus(int status) {return productRepository.findByStatus(status);}

    public void approveProduct(int productId){
        Product product = productRepository.findById(productId).orElseThrow(() -> new RuntimeException("product not found"));
        product.setStatus(1);
        productRepository.save(product);
    }

    public void rejectProduct(int productId){
        Product product = productRepository.findById(productId).orElseThrow(() -> new RuntimeException("product not found"));
        product.setStatus(2);
        productRepository.save(product);
    }

    public Product findById(int id) {return productRepository.findById(id).get();}

    public int countByShopId(int id) {return productRepository.countByShopId(id);}
}
