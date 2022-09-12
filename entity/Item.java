package entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "item")
public class Item {
    @Id
    @Column(name = "item_name", nullable = false)
    private String name;
    @Column(name = "unit_weight", nullable = false)
    private double weight;
    @Column(name = "store_name", nullable = false)
    private String storeName;
//    @Column(name = "quantity_remain", nullable = false)
//    private int quantityRemain;
    @Column(name = "unit_price", nullable = false)
    private double unitPrice;

    public Item(String name, String storeName,  double weight, double unitPrice) {
        this.name = name;
        this.weight = weight;
        this.storeName = storeName;
//        this.quantityRemain = quantityRemain;
        this.unitPrice = unitPrice;
    }

    public Item() {
    }

    public String getStoreName() {
        return storeName;
    }

    public void setStoreName(String storeName) {
        this.storeName = storeName;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getWeight() {
        return weight;
    }

    public void setWeight(double weight) {
        this.weight = weight;
    }

    public double getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(double unitPrice) {
        this.unitPrice = unitPrice;
    }

    @Override
    public String toString() {
        return "Item{" +
                "name='" + name + '\'' +
                ", weight=" + weight +
                ", storeName='" + storeName + '\'' +
                ", unitPrice=" + unitPrice +
                '}';
    }
}