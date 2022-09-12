package Dao;

import entity.Order;
import entity.Pilot;
// import entity.Pilot;
// import entity.Store;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.List;

public class OrderDao {
    static EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("db_connection");
    static EntityManager entityManager = entityManagerFactory.createEntityManager();
   
    public void addOrder(Order order) {
        EntityTransaction et = null;
        try {
            // Get hibTransaction and start
            et = entityManager.getTransaction();
            et.begin();
            entityManager.persist(order);
            et.commit();
        } catch (Exception ex) {
            // If there is an exception rollback changes
            if (et != null && et.isActive()) {
                et.rollback();
            }
            ex.printStackTrace();
            throw ex;
        }
    }

    public List<Order> getAllOrdersWithStoreName(String storeName) {
        try {
            List<Order> orders = entityManager.createQuery("select finishedOrder from Order finishedOrder where store_name =:storeName")
                    .setParameter("storeName", storeName)
                    .getResultList();
            if (orders == null || orders.size() == 0) {
                return null;
            }
            return orders;
        } catch (Exception ex) {
            ex.printStackTrace();
            throw ex;
        }
    }

    public void cleanHistoricalOrders() {
        EntityTransaction et = null;
        try {
            et = entityManager.getTransaction();
            et.begin();
            Query query = entityManager.createQuery("delete from Order finishedOrder where order_date <: currentDate");
            query.setParameter("currentDate", LocalDate.now().minusMonths(3));
            query.executeUpdate();
            et.commit();
        } catch (Exception ex) {
            ex.printStackTrace();
            throw ex;
        }
    }
}