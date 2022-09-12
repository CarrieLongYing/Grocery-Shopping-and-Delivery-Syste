package Dao;

import entity.Drone;
import entity.Item;
import entity.Pilot;


import javax.persistence.*;
import java.util.List;
import java.util.Random;

public class DroneDao {
    static EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("db_connection");
    static EntityManager entityManager = entityManagerFactory.createEntityManager();
    public String addDrone(Drone drone) {
        EntityTransaction et = null;
        String droneID = null;
        try {
            // Get hibTransaction and start
            et = entityManager.getTransaction();
            et.begin();
            // Save the messageFrame object
            entityManager.persist(drone);
            System.out.println("drone ID: " + drone.getId());
            droneID = drone.getId(); // CZY: getId() not getID()
            et.commit();
        } catch (Exception ex) {
            // If there is an exception rollback changes
            if (et != null && et.isActive()) {
                et.rollback();
            }
            ex.printStackTrace();
            throw ex;
        }
        return droneID;
    }

    public List<Drone> getDronesWithStoreName(String storeName) {
        try {
            List<Drone> drones = entityManager.createQuery("select drone from Drone drone where store_name =:storeName")
                    .setParameter("storeName", storeName)
                    .getResultList();
            if (drones == null || drones.size() == 0) {
                return null;
            }
            return drones;
        } catch (Exception ex) {
            ex.printStackTrace();
            throw ex;
        }
    }

    public void removeDroneWithStoreName(String storeName) {
        EntityTransaction et = null;
        try {
            et = entityManager.getTransaction();
            et.begin();
            Query query = entityManager.createQuery("delete from Drone drone where store_name =: storeName");
            query.setParameter("storeName",storeName);
            query.executeUpdate();
            et.commit();
        } catch (Exception ex) {
            ex.printStackTrace();
            throw ex;
        }
    }

    public Drone getDroneWithDroneId(String droneID) {
        try {
            List<Drone> drones = entityManager.createQuery("select drone from Drone drone where droneID =:droneID")
                    .setParameter("droneID", droneID)
                    .getResultList();
            if (drones == null || drones.size() == 0) {
                return null;
            }
            return drones.get(0);
        } catch (Exception ex) {
            ex.printStackTrace();
            throw ex;
        }
    }

    public void removeDroneWithStoreNameAndDroneID(String storeName, String droneID) {
        EntityTransaction et = null;
        try {
            et = entityManager.getTransaction();
            et.begin();
            Query query = entityManager.createQuery("delete from Drone drone where store_name =: storeName and droneID =: droneID");
            query.setParameter("storeName",storeName);
            query.setParameter("droneID",droneID);
            query.executeUpdate();
            et.commit();
        } catch (Exception ex) {
            ex.printStackTrace();
            throw ex;
        }
    }

    public void addDeliveries(String droneID){
        try{
            entityManager.getTransaction().begin();
            Drone drone = entityManager.find(Drone.class,droneID);
            int currDeliveries = drone.getCurrDeliveries();
            int newCurrDeliveries = currDeliveries+1;
            Query query = entityManager.createQuery("update Drone set currDeliveries=:newCurrDeliveries where id =:droneID")// "update Drone set curr_deliveries_used=:newCurrDeliveries where droneID =:droneID"
            .setParameter("newCurrDeliveries", newCurrDeliveries)
            .setParameter("droneID",droneID);
            query.executeUpdate();
            entityManager.getTransaction().commit();
        } catch(Exception ex){
            ex.printStackTrace();
            throw ex;
        }
    }

//    public void decrementOrders(String droneID){
//        try{
//            entityManager.getTransaction().begin();
//            Drone drone = entityManager.find(Drone.class,droneID);
//            int curr_numOrders = drone.getNumOfOrders();
//            int new_numOrders = curr_numOrders--;
//            Query query = entityManager.createQuery("update Drone set num_of_orders=:new_numOrders where droneID =:droneID")
//            .setParameter("new_numOrders", new_numOrders)
//            .setParameter("droneID", droneID);
//            query.executeUpdate();
//            entityManager.getTransaction().commit();
//        } catch(Exception ex){
//            ex.printStackTrace();
//            throw ex;
//        }
//    }

//    public void incrementNumOrders(String droneID){
//        try{
//            entityManager.getTransaction().begin();
//            Drone drone = entityManager.find(Drone.class,droneID);
//            int new_numOrders = drone.getNumOfOrders()+1;
//            Query query = entityManager.createQuery("update Drone set num_of_orders=:new_numOrders where droneID =:droneID")
//            .setParameter("new_numOrders", new_numOrders)
//            .setParameter("droneID", droneID);
//            query.executeUpdate();
//            entityManager.getTransaction().commit();
//        } catch(Exception ex){
//            ex.printStackTrace();
//            throw ex;
//        }
//    }

    public void decrementWeight(String droneID, double orderWeight){
        try{
            entityManager.getTransaction().begin();
            Drone drone = entityManager.find(Drone.class,droneID);
//            System.out.println("before decreasing, what's the current weight of drone: "+drone.getCurrWeight());
            double new_weight = drone.getCurrWeight()-orderWeight;
            Query query = entityManager.createQuery("update Drone set curr_weight_used=:new_weight where droneID =:droneID") //"update Drone set currWeight= currWeight - :new_weight where id =:droneID"
            .setParameter("new_weight", new_weight)
            .setParameter("droneID", droneID);
            query.executeUpdate();
            entityManager.getTransaction().commit();
//            System.out.println(drone.getCurrWeight());//CZY: test if decrementWeight can work
        } catch(Exception ex){
            ex.printStackTrace();
            throw ex;
        }

    }

    public void addWeight(String droneID, double orderWeight){// CZY: add weight to drone when item is requested
        try{
            entityManager.getTransaction().begin();
            Drone drone = entityManager.find(Drone.class,droneID);
            double new_weight = drone.getCurrWeight() + orderWeight;
            drone.setCurrWeight(new_weight);//CZY: force drone weight to be updated
            Query query = entityManager.createQuery("update Drone set curr_weight_used=:new_weight where droneID =:droneID") //            Query query = entityManager.createQuery("update Drone set currWeight=:new_weight + currWeight where id =:droneID")
                    .setParameter("new_weight", new_weight)
                    .setParameter("droneID", droneID);
            query.executeUpdate();
            entityManager.getTransaction().commit();
        } catch(Exception ex){
            ex.printStackTrace();
            throw ex;
        }
    }



    public Drone getDroneWithStoreNameAndDroneID(String storeName, String droneID) {
        try {
            List<Drone> drones = entityManager.createQuery("select drone from Drone drone where droneID =:droneID and store_name =: storeName")
                    .setParameter("droneID", droneID)
                    .setParameter("storeName", storeName)
                    .getResultList();
            if (drones == null || drones.size() == 0) {
                return null;
            }
            return drones.get(0);
        } catch (Exception ex) {
            ex.printStackTrace();
            throw ex;
        }
    }

    public Drone getAvailableDroneWithStoreName(String storeName) {
        try {
            List<Drone> drones = entityManager.createQuery("select drone from Drone drone where curr_deliveries_used < max_deliveries and store_name =: storeName")
                    .setParameter("storeName", storeName)
                    .getResultList();
            if (drones == null || drones.size() == 0) {
                return null;
            }
            return drones.get(new Random().nextInt(drones.size()));
        } catch (Exception ex) {
            ex.printStackTrace();
            throw ex;
        }
    }
}
