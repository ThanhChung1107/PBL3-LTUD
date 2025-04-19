package com.ProjectPBL3.MegarMart.Service;

import com.ProjectPBL3.MegarMart.Entity.Account;
import com.ProjectPBL3.MegarMart.Entity.Orders;
import com.ProjectPBL3.MegarMart.Repository.OrdersRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class OrdersService {
    private final OrdersRepository ordersRepository;

    public void save(Orders orders) {ordersRepository.save(orders);}

    public void updateisPaid(Orders orders) {orders.setIsPaid(1);ordersRepository.save(orders);}

    public Orders findById(int id) {return ordersRepository.findById(id).get();}

    public List<Orders> findByAccount(Account account) {return ordersRepository.findByAccount(account);}
}
