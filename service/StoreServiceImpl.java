package service;

import Dao.StoreDao;
import entity.Store;
import org.apache.log4j.Logger;
import java.util.List;

public class StoreServiceImpl implements StoreService{
    private StoreDao storeDao = new StoreDao();
    @Override
    public String addStore(Store store) {
        return storeDao.addStore(store);
    }
    @Override
    public Store getStoreWithName(String storeName) {
        return storeDao.getStoreWithName(storeName);
    }
    @Override
    public List<Store> getAllStore() {
        return storeDao.getAllStores();
    }
    @Override
    public void removeStoreWithStoreName(String storeName) {
        storeDao.removeStoreWithName(storeName);
    }
    @Override
    public void addRevenue(String storeName, double orderCost){
        storeDao.addRevenue(storeName,orderCost);
    }
}
