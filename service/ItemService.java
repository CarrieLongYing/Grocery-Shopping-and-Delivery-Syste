package service;

import entity.Item;
import java.util.List;

public interface ItemService {
    void addItem(Item item);
    Item getItemWithItemAndStoreName(String itemName, String StoreName);
    List<Item> getAllItemsWithStoreName(String storeName);
    void removeItemWithStoreName(String storeName);
    void removeItemWithStoreAndItemName(String storeName, String itemName);

    void updateSellItem(String storeName, String itemName, double weight, double unitPrice);
}

