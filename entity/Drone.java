package entity;


import service.DroneServiceImpl;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "drone")
public class Drone {
    @Column(name = "store_name", nullable = false)
    private String storeName;
    @Id
    @Column(name = "droneID", nullable = false)
    private String id;
    @Column(name = "max_weight", nullable = false)
    private double weightCapacity;
    @Column(name = "max_deliveries", nullable = false)
    private int deliveriesCapacity;
    @Column(name = "curr_weight_used", nullable = false)
    private double currWeight;
    @Column(name = "curr_deliveries_used", nullable = false)
    private int currDeliveries;
    @Transient
    private Pilot currPilot;
    @Column(name = "curr_pilotID", nullable = true)
    private Integer currPilotID;
    @Transient
    private List<Order> orders = new ArrayList<>();
//    @Column(name = "num_of_orders", nullable = true)
    @Transient
    private Integer numOfOrders;


    public Drone(String storeName, String id, double weightCapacity, int numOfDeliveries) {
        this.storeName = storeName;
        this.id = id;
        this.weightCapacity = weightCapacity;
        this.deliveriesCapacity = numOfDeliveries;
//        DroneServiceImpl droneService = new DroneServiceImpl();
//        this.numOfOrders = droneService.getDroneWithStoreNameAndDroneID(storeName, id).numOfOrders;
    }

    public Drone() {

    }

    public void removeOrder(Order order) {
        this.orders.remove(order);
//        this.numOfOrders--;
    }

    public List<Order> getOrders() {
        return orders;
    }

    public void setOrders(List<Order> orders) {
        this.orders = orders;
    }

    public void addOrder(Order order) {
        this.orders.add(order);
//        this.numOfOrders++;
    }

    public Integer getCurrPilotID() {
        return currPilotID;
    }

    public void setCurrPilotID(Integer currPilotID) {
        this.currPilotID = currPilotID;
    }

    public Integer getNumOfOrders() {
        return numOfOrders;
    }
    public void setNumOfOrders(Integer numOfOrders) {
        this.numOfOrders = numOfOrders;
    }

    public Pilot getCurrPilot() {
        return currPilot;
    }

    public void setCurrPilot(Pilot currPilot) {
        this.currPilotID = currPilot.getAccountID();
        this.currPilot = currPilot;
    }

    public void addDeliveries() {
        currDeliveries++;
    }

    public String getStoreName() {
        return storeName;
    }

    public void setStoreName(String storeName) {
        this.storeName = storeName;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public double getWeightCapacity() {
        return weightCapacity;
    }

    public void setWeightCapacity(double weightCapacity) {
        this.weightCapacity = weightCapacity;
    }

    public int getDeliveriesCapacity() {
        return deliveriesCapacity;
    }

    public void setDeliveriesCapacity(int deliveriesCapacity) {
        this.deliveriesCapacity = deliveriesCapacity;
    }

    public double getCurrWeight() {
        return currWeight;
    }

    public void setCurrWeight(double currWeight) {
        this.currWeight = currWeight;
    }

    public int getCurrDeliveries() {
        return currDeliveries;
    }

    public void setCurrDeliveries(int currDeliveries) {
        this.currDeliveries = currDeliveries;
    }

    @Override
    public String toString() {
        return "Drone{" +
                "storeName='" + storeName + '\'' +
                ", id='" + id + '\'' +
                ", weightCapacity=" + weightCapacity +
                ", deliveriesCapacity=" + deliveriesCapacity +
                ", currWeight=" + currWeight +
                ", currDeliveries=" + currDeliveries +
                ", currPilot=" + currPilot +
                ", currPilotID=" + currPilotID +
                ", orders=" + orders +
                ", numOfOrders=" + numOfOrders +
                '}';
    }
}
