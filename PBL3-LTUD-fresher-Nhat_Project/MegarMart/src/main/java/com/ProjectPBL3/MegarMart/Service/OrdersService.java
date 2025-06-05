package com.ProjectPBL3.MegarMart.Service;

import com.ProjectPBL3.MegarMart.Entity.Account;
import com.ProjectPBL3.MegarMart.Entity.Orders;
import com.ProjectPBL3.MegarMart.Repository.OrdersRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.TextStyle;
import java.util.*;


@Service
@RequiredArgsConstructor
public class OrdersService {
    private final OrdersRepository ordersRepository;

    public void save(Orders orders) {ordersRepository.save(orders);}

    public void updateisPaid(Orders orders) {orders.setIsPaid(1);ordersRepository.save(orders);}

    public Orders findById(int id) {return ordersRepository.findById(id).get();}

    public List<Orders> findByAccount(Account account) {return ordersRepository.findByAccount(account);}

    public Orders findById(Integer id) {
        return ordersRepository.findById(id).orElse(null);
    }
    public double getTotalRevenueByShop(Integer shopId) {
        Double revenue = ordersRepository.sumRevenueByShop(shopId);
        return revenue != null ? revenue : 0.0;
    }

    public double getLastMonthRevenueByShop(Integer shopId) {
        LocalDate now = LocalDate.now();
        LocalDate lastMonthStart = now.minusMonths(1).withDayOfMonth(1);
        LocalDate lastMonthEnd = now.minusMonths(1).withDayOfMonth(lastMonthStart.lengthOfMonth());

        Double revenue = ordersRepository.sumRevenueByShopAndDateRange(shopId, lastMonthStart, lastMonthEnd);
        return revenue != null ? revenue : 0.0;
    }
    public int countOrdersThisMonthByShop(Integer shopId) {
        return ordersRepository.countOrdersThisMonthByShop(shopId);
    }
    public int countOrdersByShop(Integer shopId) {
        return ordersRepository.countByShop(shopId);
    }

    public int countLastMonthOrdersByShop(Integer shopId) {
        LocalDate now = LocalDate.now();
        LocalDate lastMonthStart = now.minusMonths(1).withDayOfMonth(1);
        LocalDate lastMonthEnd = now.minusMonths(1).withDayOfMonth(lastMonthStart.lengthOfMonth());
        return ordersRepository.countByShopAndDateRange(shopId, lastMonthStart, lastMonthEnd);
    }

    public Map<String, Double> getMonthlyRevenueByShop(Integer shopId, int months) {
        Map<String, Double> result = new LinkedHashMap<>();
        LocalDate now = LocalDate.now();

        for (int i = months - 1; i >= 0; i--) {
            LocalDate monthStart = now.minusMonths(i).withDayOfMonth(1);
            LocalDate monthEnd = monthStart.withDayOfMonth(monthStart.lengthOfMonth());
            String monthLabel = monthStart.getMonth().getDisplayName(TextStyle.SHORT, Locale.getDefault())
                    + " " + monthStart.getYear();

            double revenue = ordersRepository.sumRevenueByShopAndDateRange(shopId, monthStart, monthEnd);
            result.put(monthLabel, revenue);
        }

        return result;
    }
}
