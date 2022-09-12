package Dao;

import entity.Pilot;
import entity.Store;
import entity.User;

import javax.persistence.*;
import java.util.List;
import java.util.Random;

public class PilotDao {
    static EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("db_connection");
    static EntityManager entityManager = entityManagerFactory.createEntityManager();
    public int addPilot(Pilot pilot) {
        EntityTransaction et = null;
        int accountId = Integer.MIN_VALUE;
        try {
            // Get hibTransaction and start
            et = entityManager.getTransaction();
            et.begin();
            // Save the messageFrame object
            entityManager.persist(pilot);

            System.out.println("pilot licenseID: " + pilot.getLicenseId());
            System.out.println("pilot default password: defaultPW");
            accountId = pilot.getAccountID();
            et.commit();
        } catch (Exception ex) {
            // If there is an exception rollback changes
            if (et != null && et.isActive()) {
                et.rollback();
            }
            ex.printStackTrace();
            throw ex;
        }
        return accountId;
    }

    public Pilot getPilotWithLicenseId(String licenseID) {
        try {
            List<Pilot> pilots = entityManager.createQuery("select pilot from Pilot pilot where licenseID =:licenseID")
                    .setParameter("licenseID", licenseID)
                    .getResultList();
            if (pilots == null || pilots.size() == 0) {
                return null;
            }
            return pilots.get(0);
        } catch (Exception ex) {
            ex.printStackTrace();
            throw ex;
        }
    }

    public void addNumOfDeliveries(int pilotAccountID){
        try{
            entityManager.getTransaction().begin();
            Pilot currPilot = entityManager.find(Pilot.class,pilotAccountID);
            int currDeliveries = currPilot.getNumOfDeliveries();
            int new_NumDeliveries = currDeliveries+1;
            Query query = entityManager.createQuery("update Pilot set numOfDeliveries =:new_NumDeliveries where accountID =:pilotAccountID")
            .setParameter("new_NumDeliveries", new_NumDeliveries)
            .setParameter("pilotAccountID",pilotAccountID);
            query.executeUpdate();
            entityManager.getTransaction().commit();
        }catch(Exception ex){
            ex.printStackTrace();
            throw ex;
        }
    }

    public Pilot findAvailablePilot(){
        try{
            List<Pilot> pilots = entityManager.createQuery("select pilot from Pilot pilot where pilot.droneId is null") //select pilot from Pilot pilot where current_droneID = :droneID
            // .setParameter("droneID",null) //"select pilot from Pilot pilot where pilot.droneId is NULL"
            .getResultList();
            return pilots.get(new Random().nextInt(pilots.size())); // CZY: need to pick a pilot with no drone assigned yet, so its current_droneID = null. droneId or current_droneID?????
        }catch(Exception ex){
            ex.printStackTrace();
            throw ex;
        }
    }

    public List<Pilot> getAllPilots() {
        try {
            List<Pilot> pilots = entityManager.createQuery("select pilot from Pilot pilot")
                    .getResultList();
            return pilots;
        } catch (Exception ex) {
            ex.printStackTrace();
            throw ex;
        }
    }

    public void removePilotWithAccountID(int accountID) {
        EntityTransaction et = null;
        try {
            et = entityManager.getTransaction();
            et.begin();
            Query query = entityManager.createQuery("delete from Pilot pilot where accountID =: accountID");
            query.setParameter("accountID", accountID);
            query.executeUpdate();
            et.commit();
        } catch (Exception ex) {
            ex.printStackTrace();
            throw ex;
        }
    }
    public void freePilot(String currDroneId, int pilotAccountID){
        try{
            entityManager.getTransaction().begin();
            Query query1 = entityManager.createQuery("update Drone set curr_pilotID =:pilotAccountID where droneID =:currDroneId")
                    .setParameter("pilotAccountID", null)
                    .setParameter("currDroneId",currDroneId);
            query1.executeUpdate();

            Query query = entityManager.createQuery("update Pilot set droneId =:droneID where accountID =:pilotAccountID")
                    .setParameter("droneID", null)
                            .setParameter("pilotAccountID",pilotAccountID);
            query.executeUpdate();
            entityManager.getTransaction().commit();
        }catch(Exception ex){
            ex.printStackTrace();
            throw ex;
        }
    }

    public Pilot getPilotWithAccountID(int accountID) {
        try {
            List<Pilot> pilots = entityManager.createQuery("select pilot from Pilot pilot where accountID =:accountID")
                    .setParameter("accountID", accountID)
                    .getResultList();
            if (pilots == null || pilots.size() == 0) {
                return null;
            }
            return pilots.get(0);
        } catch (Exception ex) {
            ex.printStackTrace();
            throw ex;
        }
    }
}
