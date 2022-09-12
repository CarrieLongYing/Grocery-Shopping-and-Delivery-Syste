package service;
import entity.Drone;
import java.util.List;
public interface DroneService {
    void addDrone(Drone drone);
    Drone getDroneWithDroneID(String droneID);
    void addDeliveries(String droneID);  
//    void decrementOrders(String droneID);
//    void incrementNumOrders(String droneID);
    void decrementWeight(String droneID, double orderWeight);
    List<Drone> getDronesWithStoreName(String storeName);  
    void addWeight(String droneID, double orderWeight);
    void removeDroneWithStoreName(String storeName);
    void removeDroneWithStoreAndDroneID(String storeName, String droneID);
    Drone getDroneWithStoreNameAndDroneID(String storeName, String droneID);
    Drone getAvailableDroneWithStoreName(String storeName);
}
