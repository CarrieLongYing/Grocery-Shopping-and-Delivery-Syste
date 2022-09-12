package service;

import Dao.ItemDao;
import entity.Item;
import java.util.List;

public class ItemServiceImpl implements ItemService{
    ItemDao itemDao = new ItemDao();
    @Override
    public void addItem(Item item) {
        itemDao.addItem(item);
    }

    @Override
    public Item getItemWithItemAndStoreName(String itemName, String storeName) {
        return itemDao.getItemWithItemAndStoreName(itemName, storeName);
    }

    @Override
    public List<Item> getAllItemsWithStoreName(String storeName) {
        return itemDao.getAllItemsWithStoreName(storeName);
    }

    @Override
    public void removeItemWithStoreName(String storeName) {
        itemDao.removeItemWithStoreName(storeName);
    }

    @Override
    public void removeItemWithStoreAndItemName(String storeName, String itemName) {
        itemDao.removeItemWithStoreAndItemName(storeName, itemName);
    }

    @Override
    public void updateSellItem(String storeName, String itemName, double weight, double unitPrice) {
        itemDao.updateSellItem(storeName, itemName, weight, unitPrice);
    }
}

