package com.ProjectPBL3.MegarMart.Service;

import com.ProjectPBL3.MegarMart.Entity.Account;
import com.ProjectPBL3.MegarMart.Entity.Cart;
import com.ProjectPBL3.MegarMart.Entity.Product;
import com.ProjectPBL3.MegarMart.Repository.CartRepository;
import com.ProjectPBL3.MegarMart.Repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CartService {
    private final CartRepository cartRepository;
    private final ProductRepository productRepository;

    public Cart findByAccount(Account account) {return cartRepository.findByAccount(account).get();}

    public void addProductToCart(Account account,Product product){
        Cart cart = cartRepository.findByAccount(account)
                .orElseGet(() -> {
                    Cart newCart = new Cart();
                    newCart.setAccount(account);
                    return cartRepository.save(newCart);
                });

        if(!cart.getProducts().contains(product)){
            cart.getProducts().add(product);
        }
        cartRepository.save(cart);
    }

    public List<Product> getAllCartByAccount(Account account){
        Cart cart = cartRepository.findByAccount(account).orElse(null);
        if (cart != null) {
            return cart.getProducts();
        }
        return new ArrayList<>(); // Trả về danh sách rỗng nếu không có cart
    }

    public void deleteProductFromCart(Account account, int productId) {
        Cart cart = cartRepository.findByAccount(account).orElse(null);
        if (cart != null) {
            Product product = productRepository.findById(productId).orElse(null);
            if (product != null && cart.getProducts().contains(product)) {
                cart.getProducts().remove(product); // Xóa khỏi list
                cartRepository.save(cart); // Lưu lại
            }
        }
    }
}
