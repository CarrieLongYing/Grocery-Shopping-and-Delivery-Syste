package entity;

public class Line {
    private String itemName;
    private int quantity;
    private double cost;
    private double weight;

    public Line(String itemName, int itemQuantity, double lineCost, double lineWeight) {
        this.itemName = itemName;
        this.quantity = itemQuantity;
        this.cost = lineCost;
        this.weight = lineWeight;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
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
}
