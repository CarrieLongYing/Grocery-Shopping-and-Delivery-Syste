package entity;


import service.DroneServiceImpl;
import service.ItemServiceImpl;
import service.UserServiceImpl;

import javax.persistence.*;
import java.text.DecimalFormat;
import java.util.*;


@Entity
@Table(name = "store")
public class Store {
    @Id
    @Column(name = "store_name", nullable = false)
    private String name;
    @Column(name = "revenue", nullable = false)
    private double revenue;
    @Transient
    private List<Item> items = new ArrayList<>();
    @Transient
    private List<Pilot> pilots = new ArrayList<>() ;
    @Transient
    private List<Order> orders = new ArrayList<>() ;
    @Transient
    private List<Order> completedOrders = new ArrayList<>();
    public Store(String name, double avenue) {
        this.name = name;
        this.revenue = avenue;
        this.items = new ArrayList<>();
        this.pilots = new ArrayList<>();
        this.orders = new ArrayList<>();
        this.completedOrders = new ArrayList<>();
    }
    public Store() {
    }

    public Item getItemWithName(String itemName, String storeName) {
        ItemServiceImpl itemService = new ItemServiceImpl();
        Item item = itemService.getItemWithItemAndStoreName(itemName,storeName);
        return item;
    }

    public void displayOrders() {
        if (orders != null && orders.size() > 0) {
            Collections.sort(orders, Comparator.comparing(Order::getOrderId));
            for (Order order : orders) {
//            orderID:purchaseA
//            item_name:pot_roast,total_quantity:3,total_cost:27,total_weight:15
                System.out.println("orderID:" + order.getOrderId());
                order.displayLines();
            }
        }

    }
    public Order getOrderWithId(String orderId) {
        if(orders == null || orders.size()==0){ // CZY: is it necessary to add a null check here in case it will be nullPointerException? 
            return null;
        }
        
        for (Order order : orders) {
            if (order.getOrderId().equals(orderId)) {
                return order;
            }
        }
        return null;
    }

    public void addOrder(Order order) { // CZY: see if this can bind the order and drone together
        orders.add(order);
        Drone drone = order.getDrone();
        drone.getOrders().add(order);
    }

    public void displayDrones() {
        DroneServiceImpl droneService = new DroneServiceImpl();
        List<Drone> drones = droneService.getDronesWithStoreName(this.name);
        DecimalFormat format = new DecimalFormat("0.#");
        Collections.sort(drones, Comparator.comparing(Drone::getId));
        for (Drone drone : drones) {
            if (drone.getCurrPilot() == null) {
                //droneID:1,total_cap:40,num_orders:0,remaining_cap:40,trips_left:3
                System.out.println("droneID:" + drone.getId() + ",total_cap:" + format.format(drone.getWeightCapacity()) +  ",remaining_cap:" + format.format(drone.getWeightCapacity() - drone.getCurrWeight()) +
                        ",trips_left:" + (drone.getDeliveriesCapacity() - drone.getCurrDeliveries())); //",num_orders:" + drone.getNumOfOrders() +
            } else {
                UserServiceImpl userService = new UserServiceImpl();
                User user = userService.getUserByAccountID(drone.getCurrPilotID());
                System.out.println("droneID:" + drone.getId() + ",total_cap:" + format.format(drone.getWeightCapacity()) +  ",remaining_cap:" + format.format(drone.getWeightCapacity() - drone.getCurrWeight()) +
                        ",trips_left:" + (drone.getDeliveriesCapacity() - drone.getCurrDeliveries()) + ",flown_by:" + user.getFirstName()
                        + "_" + user.getLastName()) ; // ",num_orders:" + drone.getOrders().size() +
            }
        }
    }
    public void addSellItem(String itemName,  String storeName, double weight, double price) {
        ItemServiceImpl itemService = new ItemServiceImpl();
        itemService.addItem(new Item(itemName, storeName, weight, price));
    }

    public List<Order> getOrders() {
        return orders;
    }

    public void setOrders(List<Order> orders) {
        this.orders = orders;
    }

    public List<Order> getCompletedOrders() {
        return completedOrders;
    }

    public void setCompletedOrders(List<Order> completedOrders) {
        this.completedOrders = completedOrders;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getRevenue() {
        return revenue;
    }

    public void setRevenue(double revenue) {
        this.revenue = revenue;
    }

    public List<Item> getItems() {
        return items;
    }

    public void setItems(List<Item> items) {
        this.items = items;
    }

//    public List<Drone> getDrones() {
//        return drones;
//    }

//    public void setDrones(List<Drone> drones) {
//        this.drones = drones;
//    }

    public List<Pilot> getPilots() {
        return pilots;
    }

    public void setPilots(List<Pilot> pilots) {
        this.pilots = pilots;
    }


    public void addRevenue(double cost) {
        this.revenue += cost;
    }

    public void removeOrder(Order currOrder) {
        orders.remove(currOrder);
    }
}
