package service;

import Dao.DroneDao;
import entity.Drone;
import java.util.List;

public class DroneServiceImpl implements DroneService {
    DroneDao droneDao = new DroneDao();
    @Override
    public void addDrone(Drone drone) {
        droneDao.addDrone(drone);
    }
    @Override
    public Drone getDroneWithDroneID(String droneID){
        return droneDao.getDroneWithDroneId(droneID);
    }
    @Override
    public List<Drone> getDronesWithStoreName(String storeName) {
        return droneDao.getDronesWithStoreName(storeName);
    }
    @Override
    public void removeDroneWithStoreName(String storeName) {
        droneDao.removeDroneWithStoreName(storeName);
    }
    @Override
    public void removeDroneWithStoreAndDroneID(String storeName, String droneID) {
        droneDao.removeDroneWithStoreNameAndDroneID(storeName, droneID);
    }
    @Override
    public Drone getDroneWithStoreNameAndDroneID(String storeName, String droneID) {
        return droneDao.getDroneWithStoreNameAndDroneID(storeName,droneID);
    }

    @Override
    public Drone getAvailableDroneWithStoreName(String storeName) {
        return droneDao.getAvailableDroneWithStoreName(storeName);
    }

    @Override
    public void addDeliveries(String droneID){
        droneDao.addDeliveries(droneID);
    }
//    @Override
//    public void decrementOrders(String droneId){
//        droneDao.decrementOrders(droneId);
//    }
//    @Override
//    public void incrementNumOrders(String droneID){
//        droneDao.incrementNumOrders(droneID);
//    }
    @Override
    public void decrementWeight(String droneId, double orderWeight){
        droneDao.decrementWeight(droneId,orderWeight);
    }
    @Override
    public void addWeight(String droneID, double orderWeight){droneDao.addWeight(droneID,orderWeight);}
}
