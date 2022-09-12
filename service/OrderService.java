package service;

import entity.Order;

import java.util.List;

public interface OrderService {
    void addOrder(Order order);

    List<Order> getAllOrdersWithStoreName(String storeName);

    void cleanHistoricalOrders();
}
