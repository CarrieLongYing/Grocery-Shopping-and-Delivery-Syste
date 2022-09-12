package Dao;

import entity.Store;
import javax.persistence.*;
import java.util.List;

public class StoreDao {
    static EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("db_connection");
    static EntityManager entityManager = entityManagerFactory.createEntityManager();
    public String addStore(Store store) {
        EntityTransaction et = null;
        // int accountId = Integer.MIN_VALUE;
        String storeName;
        try {
            // Get hibTransaction and start
            et = entityManager.getTransaction();
            et.begin();
            // Save the messageFrame object
            entityManager.persist(store);
            System.out.println("store name: " + store.getName());
            storeName = store.getName();
            et.commit();
        } catch (Exception ex) {
            // If there is an exception rollback changes
            if (et != null && et.isActive()) {
                et.rollback();
            }
            ex.printStackTrace();
            throw ex;
        }
        return storeName;
    }


    public Store getStoreWithName(String storeName) {
        try {
            List<Store> stores = entityManager.createQuery("select store from Store store where store_name =:storeName")
                    .setParameter("storeName", storeName)
                    .getResultList();
            if (stores == null || stores.size() == 0) {
                return null;
            }
            return stores.get(0);
        } catch (Exception ex) {
            ex.printStackTrace();
            throw ex;
        }
    }


    public List<Store> getAllStores() {
        try {
            List<Store> stores = entityManager.createQuery("select store from Store store")
                    .getResultList();
            return stores;
        } catch (Exception ex) {
            ex.printStackTrace();
            throw ex;
        }
    }

    public void removeStoreWithName(String storeName) {
        EntityTransaction et = null;
        try {
            et = entityManager.getTransaction();
            et.begin();
            Query query = entityManager.createQuery("delete from Store store where store_name =: storeName");
            query.setParameter("storeName",storeName);
            query.executeUpdate();
            et.commit();
        } catch (Exception ex) {
            ex.printStackTrace();
            throw ex;
        }
    }

    public void addRevenue(String storeName, double orderCost){//CZY: is it better to return the new store revenue?
        try{
            entityManager.getTransaction().begin();
            Query query = entityManager.createQuery("update Store set revenue =:newStoreRevenue + revenue where store_name=:storeName")
            .setParameter("newStoreRevenue", orderCost)
            .setParameter("storeName",storeName);
            query.executeUpdate();
            entityManager.getTransaction().commit();
        }catch (Exception ex) {
            ex.printStackTrace();
            throw ex;
        }
    }

}
