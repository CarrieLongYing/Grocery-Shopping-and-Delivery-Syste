package Dao;

import entity.Item;
import javax.persistence.*;
import java.util.List;

public class ItemDao {
    static EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("db_connection");
    static EntityManager entityManager = entityManagerFactory.createEntityManager();
    public Item getItemWithItemAndStoreName(String itemName, String storeName) {
        try {
            List<Item> items = entityManager.createQuery("select item from Item item where item_name =:itemName and store_name =: storeName")
                    .setParameter("itemName", itemName)
                    .setParameter("storeName", storeName)
                    .getResultList();
            if (items == null || items.size() == 0) {
                return null;
            }
            return items.get(0);
        } catch (Exception ex) {
            ex.printStackTrace();
            throw ex;
        }
    }
    public void addItem(Item item) {
        EntityTransaction et = null;
        try {
            // Get hibTransaction and start
            et = entityManager.getTransaction();
            et.begin();
            // Save the messageFrame object
            entityManager.persist(item);
            System.out.println("item name: " + item.getName());
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

    public List<Item> getAllItemsWithStoreName(String storeName) {
        try {
            List<Item> items = entityManager.createQuery("select item from Item item where store_name =: storeName")
                    .setParameter("storeName", storeName)
                    .getResultList();
            if (items == null || items.size() == 0) {
                return null;
            }
            return items;
        } catch (Exception ex) {
            ex.printStackTrace();
            throw ex;
        }
    }

    public void removeItemWithStoreName(String storeName) {
        EntityTransaction et = null;
        try {
            et = entityManager.getTransaction();
            et.begin();
            Query query = entityManager.createQuery("delete from Item item where store_name =: storeName");
            query.setParameter("storeName",storeName);
            query.executeUpdate();
            et.commit();
        } catch (Exception ex) {
            ex.printStackTrace();
            throw ex;
        }
    }
    public void removeItemWithStoreAndItemName(String storeName, String itemName) {
        EntityTransaction et = null;
        try {
            et = entityManager.getTransaction();
            et.begin();
            Query query = entityManager.createQuery("delete from Item item where store_name =: storeName and item_name =: itemName");
            query.setParameter("storeName", storeName);
            query.setParameter("itemName", itemName);

            query.executeUpdate();
            et.commit();
        } catch (Exception ex) {
            ex.printStackTrace();
            throw ex;
        }
    }

    public void updateSellItem(String storeName, String itemName, double weight, double unitPrice) {
        try {
            entityManager.getTransaction().begin();
            Query query = entityManager.createQuery("update Item set unit_weight = :weight, unit_price =: unitPrice where store_name=:storeName and item_name =: itemName")
                    .setParameter("weight", weight)
                    .setParameter("unitPrice",unitPrice)
                    .setParameter("storeName", storeName)
                    .setParameter("itemName",itemName);
            query.executeUpdate();
            entityManager.getTransaction().commit();
        } catch (Exception ex) {
            ex.printStackTrace();
            throw ex;
        }
    }
}
