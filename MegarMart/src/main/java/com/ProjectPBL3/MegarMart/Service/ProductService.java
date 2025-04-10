package com.ProjectPBL3.MegarMart.Service;

import com.ProjectPBL3.MegarMart.Entity.Category;
import com.ProjectPBL3.MegarMart.Entity.Product;
import com.ProjectPBL3.MegarMart.Repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

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
}
