package com.ProjectPBL3.MegarMart.Service;

import com.ProjectPBL3.MegarMart.Entity.OrderDetail;
import com.ProjectPBL3.MegarMart.Entity.Product;
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

