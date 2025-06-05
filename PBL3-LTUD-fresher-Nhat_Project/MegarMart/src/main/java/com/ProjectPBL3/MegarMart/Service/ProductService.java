package com.ProjectPBL3.MegarMart.Service;

import com.ProjectPBL3.MegarMart.Entity.*;
import com.ProjectPBL3.MegarMart.Repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductService {
    private final CategoryService categoryService;
    private final FileSystemStorageService storageService;
    private final OrderDetailService orderDetailService;
    private final ProductRepository productRepository;

    public void save(Product product, MultipartFile file) {
        storageService.store(file);
        String filename = file.getOriginalFilename();
        product.setImageurl(filename);
        productRepository.save(product);
    }
    public Product findById(Integer id){
        return productRepository.findById(id).orElseThrow(() -> new RuntimeException("product not found"));
    }
    public void update(Product product) {productRepository.save(product);}

    public List<Product> findAll() {return productRepository.findAll();}

    public List<Product> findByShop(Shop shop) {return productRepository.findByShop(shop);}

    public List<Product> findByShopAndStatus(Shop shop) {
        return productRepository.findByShopAndStatus(shop, 1); // status = 1
    }

    public List<Product> findByStatus(int status) {return productRepository.findByStatus(status);}

    public List<Product> findByStatusAndCategory(int status,Category category)
    {
        return productRepository.findByStatusAndCategory(status,category);
    }

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


    public List<Product> searchProduct(String productname) {
        return productRepository.searchProduct(productname);
    }
    public List<Product> searchProductByNameAndShop(String productname, Shop shop) {
        return productRepository.searchProductByNameAndShop(productname,shop);
    }

    public Page<Product> getAll(Integer pageNo) {
        Pageable pageable = PageRequest.of(pageNo-1,10);
        return productRepository.findAll(pageable);
    }

    public Page<Product> searchProduct(String keyword, Integer pageNo) {
        List<Product> list = searchProduct(keyword);
        Pageable pageable = PageRequest.of(pageNo-1,10);
        Integer start = (int) pageable.getOffset();
        Integer end = (int) ((pageable.getOffset()+pageable.getPageSize()) > list.size() ? list.size() : pageable.getOffset()+pageable.getPageSize());
        list = list.subList(start,end);
        return new PageImpl(list,pageable,searchProduct(keyword).size());}
    public boolean delete(Integer id) {
        if (orderDetailService.existsByProductId(id)) {
            return false;
        }
        productRepository.deleteById(id);
        return true;
    }
    public Page<Product> findByStatusAndCategory(int status, Category category, int page) {
        Pageable pageable = PageRequest.of(page - 1, 8); // 8 sản phẩm mỗi trang (tuỳ bạn)
        return productRepository.findByStatusAndCategory(status, category, pageable);
    }
    public Page<Product> searchProductByCategory(String keyword, Category category, int page) {
        Pageable pageable = PageRequest.of(page - 1, 8);
        return productRepository.searchByKeywordAndCategory(keyword, category, pageable);
    }

    public Page<Product> findByShop(Shop shop, Pageable pageable) {
        return productRepository.findByShop(shop, pageable);
    }
    public Page<Product> getProductsByShop(Shop shop, int page, int size) {
        Pageable pageable = PageRequest.of(page - 1, size);
        return productRepository.findByShop(shop, pageable);
    }
    public Page<Product> searchProductsByShopAndName(Shop shop, String keyword, int page, int size) {
        Pageable pageable = PageRequest.of(page - 1, size);
        return productRepository.findByShopAndNameContaining(shop, keyword, pageable);
    }
    public Page<Product> filterProductsByShopAndStatus(Shop shop, Integer status, int page, int size) {
        Pageable pageable = PageRequest.of(page - 1, size);
        return productRepository.findByShopAndStatus(shop, status, pageable);
    }
    public Page<Product> searchAndFilterProducts(Shop shop, String keyword, Integer status, int page, int size) {
        Pageable pageable = PageRequest.of(page - 1, size);
        return productRepository.findByShopAndNameContainingAndStatus(shop, keyword, status, pageable);
    }
    public long countProductsByStatus(Shop shop, Integer status) {
        return productRepository.countByShopAndStatus(shop, status);
    }
    public Page<Product> getFilteredProducts(Shop shop, Category category, String sort, String keyword, int page, int size) {
        Sort sortOption = Sort.by("price");
        sortOption = "desc".equalsIgnoreCase(sort) ? sortOption.descending() : sortOption.ascending();
        Pageable pageable = PageRequest.of(page - 1, size, sortOption);

        if (keyword != null && !keyword.trim().isEmpty()) {
            return productRepository.findByShopAndOptionalCategoryAndKeyword(shop, category, keyword.trim(), pageable);
        } else {
            return productRepository.findByShopAndOptionalCategory(shop, category, pageable);
        }
    }


}
