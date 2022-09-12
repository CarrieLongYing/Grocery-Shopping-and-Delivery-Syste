package entity;

import service.DroneServiceImpl;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.*;

@Entity
@Table(name="finishedOrder")
public class Order {
    @Id
    @Column(name = "orderID", nullable = false)
    private String orderId;
    @Column(name = "store_name", nullable = true)
    private String storeName;
    @Column(name = "accountID", nullable = true)
    private int accountID;
    @Column(name = "cost", nullable = true) //  CZY: line cost vs total cost
    private double cost;
    @Column(name = "weight", nullable = true) // CZY: line weight vs total weight
    private double weight;
    @Column(name = "order_date",nullable = true)
    private String date;

    @Transient
    private List<Line> lines;
    @Transient
    private Drone drone;
    // @Column(name = "account", nullable = false)
    @Transient
    private String customerAccount;
    @Transient
    private List<String> itemNames; // CZY: if only storing the total cost and total weight, no need for item name.. otherwise, need to store line item name, line weight and cost


    public Order(String storeName, String orderId, Drone drone, int customerId) {
        this.storeName = storeName;
        this.orderId = orderId;
        this.drone = drone;
        this.accountID = customerId;
//        this.weight = 0.0; // CZY: when the order is first initiated, the starting weight is 0.0
        this.itemNames = new ArrayList<>();
        this.lines = new ArrayList<>();
   }
   public Order(String storeName, String orderID, int accountID, double totalCost, double totalWeight, String date){ // CZY: a constructor including the drone and total weight and cost
        this.storeName = storeName;
        this.orderId = orderID;
        this.accountID = accountID;
        this.cost = totalCost;
        this.weight = totalWeight; //CZY: how to only include droneID in this table
        this.date = date;
   }
   public Order(){

   }
   public void addCost(double newCost) {
        this.cost += newCost;
   }
   public void addWeight(double newWeight) {
        this.weight += newWeight;
//        this.drone.addWeight(newWeight);
       DroneServiceImpl droneService = new DroneServiceImpl();
       droneService.addWeight(this.drone.getId(),newWeight);
   }
    public double getCost() {
        return cost;
    }

    public void setCost(double cost) {
        this.cost = cost;
    }

    public double getWeight() {
        return weight;
    }

    public void setWeight(double weight) {
        this.weight = weight;
    }

    public String getStoreName() {
        return storeName;
    }

    public void setStoreName(String storeName) {
        this.storeName = storeName;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public Drone getDrone() {
        return drone;
    }

    public void setDrone(Drone drone) {
        this.drone = drone;
    }

    public List<String> getItemNames() {
        return itemNames;
    }

    public void setItemNames(List<String> itemNames) {
        this.itemNames = itemNames;
    }

    public List<Line> getLines() {
        return lines;
    }

    public void setLines(List<Line> lines) {
        this.lines = lines;
    }

    public String getCustomerAccount() {
        return customerAccount;
    }

    public void setCustomerAccount(String customerAccount) {
        this.customerAccount = customerAccount;
    }

    public int getAccoutnID() {
        return accountID;
    }
    public void setAccoutnID(int accoutnID) {
        this.accountID = accountID;
    }
    public String getItemWithName(String itemName) {

        for (String name : itemNames) {
            if (name.equals(itemName)) {
                return name;
            }
        }
        return null;
    }
    public void addLine(String itemName, int itemQuantity, double lineCost, double lineWeight) {
        itemNames.add(itemName);
        Line line = new Line(itemName, itemQuantity, lineCost, lineWeight);
        lines.add(line);
    }

    @Override
    public String toString() {
        return "Order{" +
                "orderId='" + orderId + '\'' +
                ", storeName='" + storeName + '\'' +
                ", cost=" + cost +
                ", weight=" + weight +
                ", date='" + date + '\'' +
                ", itemNames=" + itemNames +
                '}';
    }

    public void displayLines() {
//        item_name:pot_roast,total_quantity:3,total_cost:27,total_weight:15
        DecimalFormat format = new DecimalFormat("0.#");
        for (Line line : lines) {
            System.out.println("item_name:" + line.getItemName() + ",total_quantity:" + line.getQuantity()
            + ",total_cost:" + format.format(line.getCost()) + ",total_weight:" + format.format(line.getWeight()));
        }
    }
}
