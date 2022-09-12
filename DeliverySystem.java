import entity.*;

import org.apache.log4j.Logger;
import service.*;

import java.text.DecimalFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class DeliverySystem {

    private final Logger log = Logger.getLogger(DeliverySystem.class);
    static StoreServiceImpl storeService = new StoreServiceImpl();
    static Store currStore;
    public DeliverySystem() {
    }
    public void displayPilots() {
        PilotServiceImpl pilotService = new PilotServiceImpl();
        List<Pilot> pilots = pilotService.getAllPilots();
        UserServiceImpl userService = new UserServiceImpl();
        for (Pilot currPilot : pilots) {
            User user = userService.getUserByAccountID(currPilot.getAccountID());
            System.out.println("name:" + user.getFirstName() + "_" + user.getLastName() + ",phone:" + user.getPhoneNumber()
                    + ",taxID:" + currPilot.getTaxId() + ",licenseID:" + currPilot.getLicenseId() + ",experience:" + currPilot.getNumOfDeliveries());
        }
        System.out.println("OK:display_completed");
        log.info("OK:display_Pilots_completed");
    }

    public void itemRequest(String storeName, String orderId, String itemName, int itemQuantity, int accountID) {
        storeService = new StoreServiceImpl();
        currStore = storeService.getStoreWithName(storeName);
        if (currStore == null) {
            System.out.println("ERROR:store_identifier_does_not_exist");
            log.error("ERROR:itemRequest failed, store_identifier_does_not_exist");
            return;
        }
        Order currOrder = currStore.getOrderWithId(orderId);
        if (currOrder == null) {
            System.out.println("ERROR:order_identifier_does_not_exist");
            log.error("ERROR:itemRequest failed, order_identifier_does_not_exist");
            return;
        }
        ItemServiceImpl itemServiceImpl = new ItemServiceImpl();
        Item requestItem = itemServiceImpl.getItemWithItemAndStoreName(itemName, storeName);
        if (requestItem == null) {
            System.out.println("ERROR:item_identifier_does_not_exist");
            log.error("ERROR:itemRequest failed, item_identifier_does_not_exist");
            return;
        }
        String currItemName = currOrder.getItemWithName(itemName);
        if (currItemName != null) {
            System.out.println("ERROR:item_already_ordered");
            log.error("ERROR:itemRequest failed, item_already_ordered");
            return;
        }
        // Double cost = itemPrice * itemQuantity;
        Double cost = requestItem.getUnitPrice() * itemQuantity; // CZY: for request_item, no need to pass in the unit price since it can be retrieved from table. 
        Double weight = requestItem.getWeight() * itemQuantity;

        // Customer currCustomer = getCustomerWithAccount(currOrder.getCustomerAccount());// CZY: can get the customer from table 
        CustomerServiceImpl customerServiceImpl = new CustomerServiceImpl();
        Customer currCustomer = customerServiceImpl.getCustomerWithAccountID(accountID);
        if (cost + currOrder.getCost() > currCustomer.getCredit()) {
            System.out.println("ERROR:customer_cant_afford_new_item");
            log.error("ERROR:itemRequest failed, customer_cant_afford_new_item");
            return;
        }
        Drone currDrone = currOrder.getDrone();
        if (weight + currOrder.getWeight() > currDrone.getWeightCapacity()) {
            System.out.println(currDrone.getId()); // CZY: try this to debug
            System.out.println("ERROR:drone_cant_carry_new_item");
            log.error("ERROR:itemRequest failed, drone_cant_carry_new_item");
            return;
        }
        currOrder.addCost(cost);
        currOrder.addWeight(weight);
        currOrder.addLine(itemName, itemQuantity, cost, weight);
        System.out.println("OK:change_completed");
        log.info("Info:itemRequest change_completed");
    }
    public void displayOrderOfStore(String storeName, String orderID){
        currStore = storeService.getStoreWithName(storeName);
        if (currStore == null) {
            System.out.println("ERROR:store_identifier_does_not_exist");
            log.error("ERROR:displayOrderOfStore failed, store_identifier_does_not_exist");
            return;
        }
        Order currOrder = currStore.getOrderWithId(orderID);
        currOrder.displayLines();
        System.out.println("OK:display_completed");
        log.info("Info:order_display_completed");
    }
    public void cancelOrder(String storeName, String orderId) {
//        storeService = new StoreServiceImpl();
        currStore = storeService.getStoreWithName(storeName);
        if (currStore == null) {
            System.out.println("ERROR:store_identifier_does_not_exist");
            log.error("ERROR:displayOrderOfStore failed, store_identifier_does_not_exist");
            return;
        }
        Order currOrder = currStore.getOrderWithId(orderId);
        if (currOrder == null) {
            System.out.println("ERROR:order_identifier_does_not_exist");
            log.error("ERROR:cancelOrder failed, order_identifier_does_not_exist");
            return;
        }
        currStore.removeOrder(currOrder);
        CustomerServiceImpl customerService = new CustomerServiceImpl();
        Customer currCustomer = customerService.getCustomerWithAccountID(currOrder.getAccoutnID());
        currCustomer.removeOrder(currOrder);
        Drone currDrone = currOrder.getDrone();
        String droneID = currOrder.getDrone().getId();
        DroneServiceImpl droneServiceImpl = new DroneServiceImpl();// CZY: get drone from db table
        droneServiceImpl.decrementWeight(droneID,currOrder.getWeight());
//        droneServiceImpl.decrementOrders(droneID);//numOfOrders in drone table
        currDrone.getOrders().remove(currOrder);
        System.out.println("OK:change_completed");
        log.info("Info:cancelOrder_change_completed");

    }

    public void purchaseOrder(String storeName, String orderId) {
        StoreServiceImpl storeServiceImpl = new StoreServiceImpl();
        Store currStore = storeServiceImpl.getStoreWithName(storeName);
        if (currStore == null) {
            System.out.println("ERROR:store_identifier_does_not_exist");
            log.error("ERROR:purchaseOrder failed, store_identifier_does_not_exist");
            return;
        }
        Order currOrder = currStore.getOrderWithId(orderId);
        if (currOrder == null) {
            System.out.println("ERROR:order_identifier_does_not_exist");
            log.error("ERROR:purchaseOrder failed, order_identifier_does_not_exist");
            return;
        }
        currOrder.displayLines();
        String currDroneId = currOrder.getDrone().getId();
        DroneServiceImpl droneServiceImpl = new DroneServiceImpl(); // CZY: retrieve drone from table
        Drone currDrone = droneServiceImpl.getDroneWithDroneID(currDroneId);
        if (currDrone.getCurrDeliveries() >= currDrone.getDeliveriesCapacity()) {
            System.out.println("ERROR:drone_needs_fuel");
            log.error("ERROR:purchaseOrder failed, drone_needs_fuel");
            return;
        }
        CustomerServiceImpl customerServiceImpl = new CustomerServiceImpl(); // CZY: retrieve customer from table
        Customer currCustomer = customerServiceImpl.getCustomerWithAccountID(currOrder.getAccoutnID());
        if (currCustomer.getCredit() < currOrder.getCost()) {
            currStore.removeOrder(currOrder);
            currCustomer.removeOrder(currOrder);
            return; 
        }
        Pilot currPilot = pilotFlyDrone(storeName, currDroneId);//CZY: NEED to assign a pilot when the purchase order is being executed... find the pilot before the pilot's experience level is incremented
        PilotServiceImpl pilotServiceImpl = new PilotServiceImpl();
        customerServiceImpl.decreaseCredit(currCustomer.getAccountID(), currOrder.getCost());//CZY: customerDao to interact with database
        storeServiceImpl.addRevenue(storeName,currOrder.getCost());//CZY: storeDao to update database
        droneServiceImpl.addDeliveries(currDroneId);
//        droneServiceImpl.decrementOrders(currDroneId); //CZY: once an order is delivered, the num_orders should be decremeted
        droneServiceImpl.decrementWeight(currDroneId,currOrder.getWeight()); //CZY:
        currDrone.removeOrder(currOrder);
        pilotServiceImpl.addNumOfDeliveries(currPilot.getAccountID());
        pilotServiceImpl.freePilot(currDroneId, currPilot.getAccountID()); // once an order is delivered, the pilot controls no drone..
        currStore.getCompletedOrders().add(currOrder);
        currCustomer.getCompletedOrders().add(currOrder);
        currStore.removeOrder(currOrder);
        currCustomer.removeOrder(currOrder);
        Order order = new Order(storeName, orderId, currCustomer.getAccountID(),currOrder.getCost(),currOrder.getWeight(), LocalDate.now().toString()); // CZY: an order: storeName orderID, accountID, cost and weight, no line items...
        OrderServiceImpl orderServiceImpl = new OrderServiceImpl();
        orderServiceImpl.addOrder(order);
        System.out.println("OK:change_completed");
        log.info("Info:purchaseOrder_change_completed");

    }

    public void addOrder(String storeName, int accountID) { //CZY: originally String account, but after login we already have the account information
        String orderId = accountID + LocalDateTime.now().toString();
        StoreServiceImpl storeServiceImpl = new StoreServiceImpl();
        Store currStore = storeServiceImpl.getStoreWithName(storeName);
        if (currStore == null) {
            System.out.println("ERROR:store_identifier_does_not_exist");
            log.error("ERROR:addOrder failed, store_identifier_does_not_exist");
            return;
        }
        Order currOrder = currStore.getOrderWithId(orderId); // CZY: save the orders in a data structure within the currStore class object.
        if (currOrder != null) {
            System.out.println("ERROR:order_identifier_already_exists");
            log.error("ERROR:addOrder failed, order_identifier_already_exists");
            return;
        }
        DroneServiceImpl droneServiceImpl = new DroneServiceImpl(); // CZY: get the droe through database
        Drone currDrone = droneServiceImpl.getAvailableDroneWithStoreName(storeName);
        if (currDrone == null) {
            System.out.println("ERROR:drone_identifier_does_not_exist");
            log.error("ERROR:addOrder failed, drone_identifier_does_not_exist");
            return;
        }
//        droneServiceImpl.incrementNumOrders(currDrone.getId());
        Order newOrder = new Order(storeName,orderId,currDrone,accountID);// CZY: need to associate drone with order.
        CustomerServiceImpl customerServiceImpl = new CustomerServiceImpl();
        Customer currCustomer = customerServiceImpl.getCustomerWithAccountID(accountID); // CZY: User and Customer: no type casting
        currCustomer.addOrder(newOrder);
        currStore.addOrder(newOrder);
        System.out.println("This orderID is " + orderId);
        System.out.println("OK:change_completed");
        log.info("Info:addOrder_change_completed");

    }



    public Pilot pilotFlyDrone(String storeName, String droneId) { // CZY: it is better to return a pilot to the Purchase_order function
        StoreServiceImpl storeServiceImpl = new StoreServiceImpl();//CZY: get store from database table
        Store currStore = storeServiceImpl.getStoreWithName(storeName);
        if (currStore == null) {
            System.out.println("ERROR:store_identifier_does_not_exist");
            log.error("ERROR:pilotFlyDrone failed, store_identifier_does_not_exist");
            return null;
        }
        DroneServiceImpl droneServiceImpl = new DroneServiceImpl();//CZY: get drone from database table.
        Drone currDrone = droneServiceImpl.getDroneWithDroneID(droneId);
        if (currDrone == null) {
            System.out.println("ERROR:drone_identifier_does_not_exist");
            log.error("ERROR:pilotFlyDrone failed, drone_identifier_does_not_exist");

            return null;
        }
        PilotServiceImpl pilotServiceImpl = new PilotServiceImpl();
        Pilot currPilot = pilotServiceImpl.findAvailablePilot(); // CZY: find the the available pilot by looping through all the pilots in the database...
        currPilot.setDroneId(droneId);
        currDrone.setCurrPilot(currPilot);
//        System.out.println("OK:change_completed");
        log.info("Info:pilotFlyDrone_change_completed");
        return currPilot;
    }

    public void addSellItem(String storeName, String itemName, double weight, double unitPrice) {
        StoreServiceImpl storeService = new StoreServiceImpl();
        Store currStore = storeService.getStoreWithName(storeName);
        if (currStore == null) {
            System.out.println("ERROR:store_identifier_does_not_exist");
            log.error("Add sell item failed, store_identifier_does_not_exist");
            return;
        }
        Item currItem = currStore.getItemWithName(itemName, storeName);
        if (currItem != null) {
            System.out.println("ERROR:item_identifier_already_exists");
            log.error("Add sell item failed, item_identifier_already_exists");
            return;
        }

        currStore.addSellItem(itemName, storeName,weight,unitPrice);
        System.out.println("OK:change_completed");
        log.info("Add sell item, change_completed");
    }

    public void displaySellItems(String storeName) {
        StoreServiceImpl storeService = new StoreServiceImpl();
        Store currStore = storeService.getStoreWithName(storeName);
        if (currStore == null) {
            System.out.println("ERROR:store_identifier_does_not_exist");
            log.error("Display sell items failed, store_identifier_does_not_exist");
            return;
        }
        ItemServiceImpl itemServiceImpl = new ItemServiceImpl();
        List<Item> items = itemServiceImpl.getAllItemsWithStoreName(storeName);
        Collections.sort(items, Comparator.comparing(Item::getName));
        for (Item item : items) {
            DecimalFormat format = new DecimalFormat("0.#");
            System.out.println(item.toString());
        }
        // currStore.displaySellItems(); // go to Item table in order to display all items of a particular store, not the store table.
        System.out.println("OK:display_completed");
        log.info("display_sell_items_completed");
    }
    public void addDrone(String storeName, String droneId, double weightCapacity, int numOfDeliveries) {
        StoreServiceImpl storeService = new StoreServiceImpl();
        Store currStore = storeService.getStoreWithName(storeName);
        if (currStore == null) {
            System.out.println("ERROR:store_identifier_does_not_exist");
            log.error("Add drone failed, store_identifier_does_not_exist");
            return;
        }
        DroneServiceImpl droneService = new DroneServiceImpl();
        Drone currDrone = droneService.getDroneWithStoreNameAndDroneID(storeName, droneId);

        if (currDrone != null) {
            System.out.println("ERROR:drone_identifier_already_exists");
            log.error("Add drone failed, drone_identifier_already_exists");
            return;
        }
        droneService.addDrone(new Drone(storeName, droneId, weightCapacity, numOfDeliveries));
        System.out.println("OK:change_completed");
        log.info("Add drone, change_completed");
    }
    public void displayDrones(String storeName) {
        StoreServiceImpl storeService = new StoreServiceImpl();
        Store currStore = storeService.getStoreWithName(storeName);
        if (currStore == null) {
            System.out.println("ERROR:store_identifier_does_not_exist");
            log.error("Display drone failed, store_identifier_does_not_exist");
            return;
        }
        currStore.displayDrones();
        System.out.println("OK:display_completed");
        log.info("display_drones_completed");
    }

    public void addStore(String storeName, String revenue) {
        StoreServiceImpl storeService = new StoreServiceImpl();
        Store currStore = storeService.getStoreWithName(storeName);
        if (currStore == null) {
            Store store = new Store(storeName, Double.valueOf(revenue));
            storeService.addStore(store);
        } else {
            System.out.println("ERROR:store_identifier_already_exists");
            log.error("Add store failed, store_identifier_already_exists");
            return;
        }
        System.out.println("OK:change_completed");
        log.info("Add store, change_completed");
    }

    public void displayStore(int userType) {
        StoreServiceImpl storeService = new StoreServiceImpl();
        List<Store> stores = storeService.getAllStore();
        //name:abc,revenue:3000
        DecimalFormat format = new DecimalFormat("0.#");
        for (Store store : stores) {
            if (userType == 0) {
                System.out.println("name:" + store.getName() + ",revenue:" + format.format(store.getRevenue()));
            } else {
                System.out.println("name:" + store.getName());
            }
        }
        System.out.println("OK:display_completed");
        log.info("display_all_stores_completed");
    }

    public void addPilot(String account, String firstName, String lastName, String phoneNumber, String taxId, String licenseId, String numOfDeliveries) {
        UserServiceImpl userService = new UserServiceImpl();
        User currUser = userService.getUserByAccount(account);
        if (currUser != null) {
            System.out.println("ERROR:pilot_identifier_already_exists");
            log.error("Add pilot failed, pilot_identifier_already_exists");
            return;
        }
        PilotServiceImpl pilotService = new PilotServiceImpl();
        Pilot currPilot = pilotService.getPilotWithLicenseId(licenseId);
        if (currPilot != null) {
            System.out.println("ERROR:pilot_license_already_exists");
            log.error("Add pilot failed, pilot_license_already_exists");
            return;
        }
        int accountID = userService.addUser(new User(firstName,lastName,phoneNumber,2, "defaultPW",account));
        pilotService.addPilot(new Pilot(accountID,taxId,licenseId,Integer.valueOf(numOfDeliveries)));
        System.out.println("OK:change_completed");
        log.info("Add pilot, change_completed");
    }

    public void addCustomer(String account, String firstName, String lastName, String phoneNumber, String rating, String credit) {
        UserServiceImpl userService = new UserServiceImpl();
        User currUser = userService.getUserByAccount(account);
        if (currUser != null) {
            System.out.println("ERROR:customer_identifier_already_exists");
            log.error("Add customer failed, customer_identifier_already_exists");
            return;
        }
        int accountID = userService.addUser(new User(firstName,lastName,phoneNumber,1,"defaultPW", account));
        CustomerServiceImpl customerService = new CustomerServiceImpl();
        customerService.addCustomer(new Customer(accountID, Integer.valueOf(rating), Double.valueOf(credit)));
        System.out.println("OK:change_completed");
        log.info("Add customer, change_completed");
    }

    public void displayCustomers() {
        CustomerServiceImpl customerService = new CustomerServiceImpl();
        List<Customer> customers = customerService.getAllCustomers();
        UserServiceImpl userService = new UserServiceImpl();
        for (Customer customer : customers) {
            User user = userService.getUserByAccountID(customer.getAccountID());
            //name:Alana_Apple,phone:222-222-2222,rating:4,credit:100
            DecimalFormat format = new DecimalFormat("0.#");
            System.out.println("name:" + user.getFirstName() + "_" + user.getLastName() + ",phone:"
                    + user.getPhoneNumber() + ",rating:" + customer.getRating() + ",credit:" + format.format(customer.getCredit()));
        }
        System.out.println("OK:display_completed");
        log.info("display_all_customers_completed");
    }

    public void removeStore(String storeName) {
        StoreServiceImpl storeService = new StoreServiceImpl();
        Store currStore = storeService.getStoreWithName(storeName);
        if (currStore == null) {
            System.out.println("Error: store_name_does_not_exist");
            log.error("This store does not exist");
            return;
        }
        storeService.removeStoreWithStoreName(storeName);
        ItemServiceImpl itemService = new ItemServiceImpl();
        DroneServiceImpl droneService = new DroneServiceImpl();
        itemService.removeItemWithStoreName(storeName);
        droneService.removeDroneWithStoreName(storeName);
        log.info("Successfully remove this store");
        System.out.println("OK:change_completed");
    }

    public void removeItem(String storeName, String itemName) {
        StoreServiceImpl storeService = new StoreServiceImpl();
        Store currStore = storeService.getStoreWithName(storeName);
        if (currStore == null) {
            System.out.println("Error: store_name_does_not_exist");
            log.error("This store does not exist");
            return;
        }
        ItemServiceImpl itemService = new ItemServiceImpl();
        Item currItem = itemService.getItemWithItemAndStoreName(itemName, storeName);
        if (currItem == null) {
            System.out.println("Error: item_name_does_not_exist");
            log.error("This item does not exist");
            return;
        }
        itemService.removeItemWithStoreAndItemName(storeName, itemName);
        log.info("Successfully remove this item");
        System.out.println("OK:change_completed");
    }

    public void removePilot(int accountID) {
        UserServiceImpl userService = new UserServiceImpl();
        User currUser = userService.getUserByAccountID(accountID);
        if (currUser == null) {
            System.out.println("Error: accountID_does_not_exist");
            log.error("This accountID does not exist");
            return;
        }
        if (currUser.getUserType() != 2) {
            System.out.println("Error: accountID_is_not_pilot");
            log.error("This accountID is not a pilot");
            return;
        }
        PilotServiceImpl pilotService = new PilotServiceImpl();
        userService.removeUserWithAccountID(accountID);
        pilotService.removePilotWithAccountID(accountID);
        log.info("Successfully remove this pilot");
        System.out.println("OK:change_completed");
    }

    public void removeDrone(String storeName, String droneID) {
        StoreServiceImpl storeService = new StoreServiceImpl();
        Store currStore = storeService.getStoreWithName(storeName);
        if (currStore == null) {
            System.out.println("Error: store_name_does_not_exist");
            log.error("This store does not exist");
            return;
        }
        DroneServiceImpl droneService = new DroneServiceImpl();
        Drone currDrone = droneService.getDroneWithStoreNameAndDroneID(storeName, droneID);
        if (currDrone == null) {
            System.out.println("Error: droneID_does_not_exist");
            log.error("This drone does not exist");
            return;
        }
        droneService.removeDroneWithStoreAndDroneID(storeName, droneID);
        log.info("Successfully remove this drone");
        System.out.println("OK:change_completed");
    }

    public void removeCustomer(Integer accountID) {
        UserServiceImpl userService = new UserServiceImpl();
        User currUser = userService.getUserByAccountID(accountID);
        if (currUser == null) {
            System.out.println("Error: accountID_does_not_exist");
            log.error("This accountID does not exist");
            return;
        }
        if (currUser.getUserType() != 1) {
            System.out.println("Error: accountID_is_not_customer");
            log.error("This accountID is not a customer");
            return;
        }
        userService.removeUserWithAccountID(accountID);
        CustomerServiceImpl customerService = new CustomerServiceImpl();
        customerService.removeCustomerWithAccountID(accountID);
        log.info("Successfully remove this customer");
        System.out.println("OK:change_completed");    }

    public void displayAllOrdersWithStoreName(String storeName) {
        OrderServiceImpl orderService = new OrderServiceImpl();
        List<Order> orders = orderService.getAllOrdersWithStoreName(storeName);
        double total = 0.0;
        for (Order order : orders) {
            System.out.println("order id: " + order.getOrderId() + ", order cost: " + order.getCost() + ", order weight: " + order.getWeight());
            total += order.getCost();
        }
        System.out.println("total income of the store within three months: " + total);
        log.info("total income of the store within three months: " + total);

    }

    public void cleanHistoricalOrders() {
        OrderServiceImpl orderService = new OrderServiceImpl();
        orderService.cleanHistoricalOrders();
        System.out.println("OK:change_completed");
        log.info("cleanHistoricalOrders_change_completed");
    }

    public void displayPilotInfo(int accountID) {
        PilotServiceImpl pilotService = new PilotServiceImpl();
        Pilot pilot = pilotService.getPilotWithAccountID(accountID);
        System.out.println(pilot.toString());
        System.out.println("OK:display_completed");
        log.info("displayPilotInfo_change_completed");

    }

    public void displayCustomerInfo(int accountID) {
        CustomerServiceImpl customerService = new CustomerServiceImpl();
        Customer customer = customerService.getCustomerWithAccountID(accountID);
        System.out.println(customer.toString());
        System.out.println("OK:display_completed");
        log.info("displayCustomerInfo_change_completed");

    }

    public void addCredit(int accountID, Double credit) {
        CustomerServiceImpl customerService = new CustomerServiceImpl();
        Customer customer = customerService.getCustomerWithAccountID(accountID);
        customerService.addCredit(accountID, credit);
        customer.addCredit(credit);
        System.out.println("OK:change_completed");
        log.info("addCredit_change_completed");

    }

    public void updateSellItem(String storeName, String itemName, double weight, double unitPrice) {
        StoreServiceImpl storeService = new StoreServiceImpl();
        Store currStore = storeService.getStoreWithName(storeName);
        if (currStore == null) {
            System.out.println("ERROR:store_identifier_does_not_exist");
            log.error("Add sell item failed, store_identifier_does_not_exist");
            return;
        }
        Item currItem = currStore.getItemWithName(itemName, storeName);
        if (currItem == null) {
            System.out.println("ERROR:item_identifier_does_not_exists,please add this item first.");
            log.error("Update sell item failed");
            return;
        }
        ItemServiceImpl itemService = new ItemServiceImpl();
        itemService.updateSellItem(storeName, itemName, weight, unitPrice);
        System.out.println("OK:change_completed");
        log.info("updateSellItem_change_completed");
    }
}

