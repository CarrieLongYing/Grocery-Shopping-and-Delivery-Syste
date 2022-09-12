package service;

import entity.Store;
import java.util.List;

public interface StoreService {
    String addStore(Store store);
    List<Store> getAllStore();
    void addRevenue(String storeName, double orderCost);
    void removeStoreWithStoreName(String storeName);
    Store getStoreWithName(String storeName);
}
