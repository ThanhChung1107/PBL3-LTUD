package com.ProjectPBL3.MegarMart.Service;

import com.ProjectPBL3.MegarMart.Entity.OrderDetail;
import com.ProjectPBL3.MegarMart.Entity.Product;
import com.ProjectPBL3.MegarMart.Entity.Shop;
import com.ProjectPBL3.MegarMart.Repository.OrderDetailRepository;
import org.springframework.beans.factory.annotation.Autowired;
import com.ProjectPBL3.MegarMart.Repository.OrdersRepository;
import com.ProjectPBL3.MegarMart.Repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Optional;
import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderDetailService {
    @Autowired
    private OrderDetailRepository orderDetailRepository;
    // các repository khác nếu cần
    private final OrdersRepository ordersRepository;
    private final ProductRepository productRepository;

    public OrderDetail findById(Integer id) {
        Optional<OrderDetail> optional = orderDetailRepository.findById(id);
        return optional.orElse(null);}
    public List<OrderDetail> findOrderDetailByProduct(Product pro) {
        return orderDetailRepository.findByProduct(pro);
    }

    public void save(OrderDetail orderDetail) {
        orderDetailRepository.save(orderDetail);}
    public boolean  existsByProductId(Integer Id) {
        return orderDetailRepository.existsByProductId(Id);
    }
    public Page<OrderDetail> findOrderDetailsByShop(Shop shop, Pageable pageable) {
        return orderDetailRepository.findOrderDetailsByShop(shop, pageable);
    }
    public List<OrderDetail> findOrderDetailsByShop(Shop shop) {
        return orderDetailRepository.findOrderDetailsByShop(shop);
    }
    // Lọc toàn bộ (không phân trang)
    public List<OrderDetail> findFilteredOrderDetails(Shop shop, String keyword, LocalDate fromDate, LocalDate toDate) {
        return orderDetailRepository.findFilteredOrderDetails(shop, keyword, fromDate, toDate);
    }

    // Lọc có phân trang
    public Page<OrderDetail> findFilteredOrderDetailsPage(Shop shop, String keyword, LocalDate fromDate, LocalDate toDate, Pageable pageable) {
        return orderDetailRepository.findFilteredOrderDetailsPage(shop, keyword, fromDate, toDate, pageable);
    }
    public List<OrderDetail> searchProduct(String productName) {
        List<Product> products = productRepository.searchProduct(productName);
        List<OrderDetail> orderDetails = new ArrayList<>();
        for (Product product : products) {
            List<OrderDetail> details = orderDetailRepository.findByProduct(product);
            orderDetails.addAll(details);
        }
        return orderDetails;
    }


    public Page<OrderDetail> getAll(Integer pageNo) {
        Pageable pageable = PageRequest.of(pageNo-1,5);
        return orderDetailRepository.findAll(pageable);
    }
    public Page<OrderDetail> searchProduct(String keyword, Integer pageNo) {
        List<OrderDetail> list = searchProduct(keyword);
        Pageable pageable = PageRequest.of(pageNo-1,5);
        Integer start = (int) pageable.getOffset();
        Integer end = (int) ((pageable.getOffset()+pageable.getPageSize()) > list.size() ? list.size() : pageable.getOffset()+pageable.getPageSize());
        list = list.subList(start,end);
        return new PageImpl(list,pageable,searchProduct(keyword).size());}
}

