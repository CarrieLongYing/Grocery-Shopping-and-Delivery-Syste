package entity;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
@Entity
@Table(name = "customer")
public class Customer {
    @Id
    @Column(name = "accountID", nullable = false)
    private int accountID;
    @Column(name = "rating", nullable = false)
    private int rating;//1 - 5
    @Column(name = "credit", nullable = false)
    private double credit;
    @Transient
    private List<Order> orders = new ArrayList<>(); //CZY: since we are not saving the orders in a table, we need instantiate it first? 
    @Transient
    private List<Order> completedOrders = new ArrayList<>();
    public Customer(int accountID, int rating, double credit) { // CZY: new customer constructor
        this.accountID = accountID;
        this.rating = rating;
        this.credit = credit;
        this.orders = new ArrayList<>();
        this.completedOrders = new ArrayList<>();
    }
    public Customer() {

    }
    public void addCredit(double amount) {
        credit += amount;

    }
    public void decreaseCredit(Double amount) {
        credit -= amount;
    }
    // public void addOrder(String storeName, String orderId, Drone drone, String account) { 
    //     orders.add(new Order(storeName, orderId, drone, account));
    // }
    public void addOrder(Order order) { //CZY: see if this can bind the drone and order together. 
        orders.add(order);
    }
    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }

    public double getCredit() {
        return credit;
    }

    public void setCredit(double credit) {
        this.credit = credit;
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

    public void removeOrder(Order currOrder) {
        orders.remove(currOrder);
    }

    public int getAccountID() {
        return accountID;
    }

    public void setAccountID(int accountID) {
        this.accountID = accountID;
    }

    @Override
    public String toString() {
        return "Customer{" +
                "accountID=" + accountID +
                ", rating=" + rating +
                ", credit=" + credit +
                ", orders=" + orders.toString() +
                ", completedOrders=" + completedOrders +
                '}';
    }
}
