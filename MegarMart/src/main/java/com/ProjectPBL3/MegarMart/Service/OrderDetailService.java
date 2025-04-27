package com.ProjectPBL3.MegarMart.Service;

import com.ProjectPBL3.MegarMart.Entity.OrderDetail;
import com.ProjectPBL3.MegarMart.Entity.Product;
import com.ProjectPBL3.MegarMart.Repository.OrderDetailRepository;
import org.springframework.beans.factory.annotation.Autowired;
import com.ProjectPBL3.MegarMart.Repository.OrdersRepository;
import com.ProjectPBL3.MegarMart.Repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

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

}

