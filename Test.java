

import java.time.LocalDate;
import java.util.List;

import Dao.*;
import entity.Order;
import entity.Store;
import entity.User;
import service.*;

import java.time.LocalDate;
import java.util.List;

public class Test {

    public static void main(String[] args) {
        System.setProperty("logfile.name","C:/Log4j/" + LocalDate.now() + "logs.txt");

        //        User user = new User("a","c","d",1,"2");
//        System.out.println(user.getAccountID());
//        UserServiceImpl userService = new UserServiceImpl();
////        userService.addUser(user);
//        User user = userService.getUserByAccountIDAndPW(3,"2");
//        System.out.println(user.getUserType());
//
//        StoreServiceImpl storeService = new StoreServiceImpl();
//        storeService.addStore(new Store("apple",123));

//        StoreServiceImpl storeService = new StoreServiceImpl();
//        Store currStore = storeService.getStoreWithName("alpha");
//        System.out.println(currStore.getName());
//        DroneServiceImpl droneService = new DroneServiceImpl();
//        List<Drone> drones = droneService.getDronesWithStoreName("alpha");
//        System.out.println(drones);
//        storeService.getAllStore();
//        storeService.addStore(new Store("PandaExpressFresh",900));
//        storeService.removeStoreWithStoreName("kroger");
//        System.out.println("successful");
        // StoreServiceImpl storeService = new StoreServiceImpl();
        // List<Store> stores = storeService.getAllStores();
        // System.out.println(stores.get(0).getName());

       
//         UserServiceImpl userService = new UserServiceImpl();
// //      userService.addUser(user);
//         User user = userService.getUserByAccountIDAndPW(1,"12345");
//         System.out.println(user.getFirstName());

        // PilotDao pilotDao = new PilotDao();
        // System.out.println(pilotDao.findAvailablePilot().getAccountID());
        // CustomerDao customerDao = new CustomerDao();
        // customerDao.decreaseCredit("kirving", 1.0);
//        OrderServiceImpl orderServiceImpl = new OrderServiceImpl();
//        // orderServiceImpl.addOrder(new Order("alpha", "abc111", 10, 1.5, 1.8, null));
//        orderServiceImpl.addOrder(new Order("allll", "aaaaaa", 6, 1.9, 11.0, LocalDate.now().toString()));

//        DroneDao droneDao = new DroneDao();
//        droneDao.decrementWeight("star2",1.0);
//        droneDao.addDeliveries("star1");
//        PilotDao pilotDao = new PilotDao();
//        pilotDao.addNumOfDeliveries(11);
//        PilotServiceImpl pilotService = new PilotServiceImpl();
//        pilotService.freePilot(17);

//        StoreDao storeDao = new StoreDao();
//        System.out.println(storeDao.getAllStores().get(0).getName());
//        System.out.println(storeDao.getStoreWithName("alpha").getRevenue());
        OrderServiceImpl orderService = new OrderServiceImpl();
        orderService.cleanHistoricalOrders();


    }

}
