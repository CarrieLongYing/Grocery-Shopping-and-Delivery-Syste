package service;

import Dao.OrderDao;
import entity.Order;

import java.util.List;

public class OrderServiceImpl implements OrderService {
    OrderDao orderDao = new OrderDao();
    public void addOrder(Order order){
        orderDao.addOrder(order);
    }

    @Override
    public List<Order> getAllOrdersWithStoreName(String storeName) {
        List<Order> orders = orderDao.getAllOrdersWithStoreName(storeName);
        return orders;
    }

    @Override
    public void cleanHistoricalOrders() {
        orderDao.cleanHistoricalOrders();
    }
}
