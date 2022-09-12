import entity.Customer;
import entity.User;
import org.apache.log4j.Logger;
import service.CustomerServiceImpl;
import service.UserServiceImpl;
import java.time.LocalDate;
import java.util.Scanner;

public class DeliveryService {
    private Logger log = null;
    public void commandLoop() {
        System.setProperty("logfile.name","C:/Log4j/" + LocalDate.now() + "logs.txt");
        log = Logger.getLogger(DeliveryService.class);
        Scanner commandLineInput = new Scanner(System.in);
        String wholeInputLine;
        String[] tokens;
        final String DELIMITER = ",";
        DeliverySystem deliverySystem = new DeliverySystem();
        User user = null;
        while (true) {
            try {
                // Determine the next command and echo it to the monitor for testing purposes
                wholeInputLine = commandLineInput.nextLine();
                tokens = wholeInputLine.split(DELIMITER);
                System.out.println("> " + wholeInputLine);
                if (tokens[0].contains("//")) {
                    continue;
                }
                if (tokens[0].equals("signup")) {
                    signup(tokens);
                } else if (tokens[0].equals("login")) {
                   user = login(tokens);
                }
                if (user != null && user.getUserType() == 0) {
                    adminOperations(user,deliverySystem);
                    System.out.println("Successfully logout");
                    break;
                } else if (user != null && user.getUserType() == 1) {//need display orders/customer info
                    customerOperations(user, deliverySystem);
                    System.out.println("Successfully logout");
                    break;
                } else if (user != null && user.getUserType() == 2) {
                    pilotOperations(user,deliverySystem);
                    System.out.println("Successfully logout");
                    break;
                }
             }catch (Exception e) {
                e.printStackTrace();
                System.out.println();
             }
        }
    }

    private void signup(String[] tokens) {
        UserServiceImpl userService = new UserServiceImpl();
        User user = new User();
        user.setFirstName(tokens[1]);
        user.setLastName(tokens[2]);
        user.setPhoneNumber(tokens[3]);
        if (tokens[4].equals("customer")) {
            user.setUserType(1);
        } else {
            System.out.println("Error: Wrong Account Type");
            return;
        }
        user.setAccount(tokens[5]);
        user.setPassword(tokens[6]);
        userService.addUser(user);
        if (user.getUserType() == 1) {
            CustomerServiceImpl customerService = new CustomerServiceImpl();
            customerService.addCustomer(new Customer(user.getAccountID(),0,0));
        }
        log.info(user.getAccount() + " account successfully signup");
        System.out.println("Account successfully signup, please login");

    }
    private User login(String[] tokens) {
        int currAccountID = Integer.valueOf(tokens[1]);
        String currPassword = tokens[2];
        UserServiceImpl userService = new UserServiceImpl();
        User user = userService.getUserByAccountIDAndPW(currAccountID, currPassword);
        if (user == null) {
            log.error("No accountID exist or wrong password");
            System.out.println("Error: no_user_found, No accountID exist or wrong password");
            return null;
        }
        log.info( user.getFirstName() +" are successfully login");
        System.out.println("Welcome, "+ user.getFirstName() +", you are successfully login!");
        if(user.getUserType()==0){
            System.out.println("Administrators are authorized to carry out the following: ");
            System.out.println("[make_store],      [display_stores], [remove_store], [sell_item],      [clean_historical_orders]");
            System.out.println("[display_items],   [remove_item],    [make_pilot],   [display_pilots], [remove_pilot]");
            System.out.println("[make_drone],      [display_drones], [remove_drone], [make_customer],  [display_customers]");
            System.out.println("[remove_customer], [display_orders], [update_item],  [logout]");
        }else if(user.getUserType() == 1){
            System.out.println("Customers are authorized to carry out the following: ");
            System.out.println("[display_stores], [display_items], [start_order]");
            System.out.println("[request_item],   [add_credit],    [display_order]");
            System.out.println("[purchase_order], [cancel_order],  [display_customer_info], [logout]");
        }else{
            System.out.println("Pilots are authorized to carry out the following: ");
            System.out.println("[display_pilot_info], [logout]");
        }

        return user;
    }
    
    private void pilotOperations(User user, DeliverySystem deliverySystem) {
        Scanner commandLineInput = new Scanner(System.in);
        String wholeInputLine;
        String[] tokens;
        final String DELIMITER = ",";
        while (true) {
            wholeInputLine = commandLineInput.nextLine();
            tokens = wholeInputLine.split(DELIMITER);
            if (tokens[0].equals("display_pilot_info")) {
                deliverySystem.displayPilotInfo(user.getAccountID());
            } else if (tokens[0].equals("logout")) {
                System.out.println("stop acknowledged");
                break;
            } else {
                System.out.println("command " + tokens[0] + " NOT acknowledged");
            }
        }
    }

    private void customerOperations(User user, DeliverySystem deliverySystem) {
        Scanner commandLineInput = new Scanner(System.in);
        String wholeInputLine;
        String[] tokens;
        final String DELIMITER = ",";
        while (true) {
            wholeInputLine = commandLineInput.nextLine();
            tokens = wholeInputLine.split(DELIMITER);
            if (tokens[0].equals("display_stores")) {
                deliverySystem.displayStore(user.getUserType());
            } else if (tokens[0].equals("display_items")) { 
                deliverySystem.displaySellItems(tokens[1]);
            } else if (tokens[0].equals("start_order")) {
                deliverySystem.addOrder(tokens[1],user.getAccountID()); //CZY: the user.getAccountID is passed into the system, no need to type in the customer account
            } else if (tokens[0].equals("request_item")) {
                deliverySystem.itemRequest( tokens[1], tokens[2], tokens[3], Integer.valueOf(tokens[4]), user.getAccountID());//CZY: do not need to type unit price, also pass in customer account
                // deliverySystem.itemRequest( tokens[1], tokens[2], tokens[3], Integer.valueOf(tokens[4]), Double.valueOf(tokens[5]));
//                  System.out.println("store: " + tokens[1] + ", order: " + tokens[2] + ", item: " + tokens[3] + ", quantity: " + tokens[4] + ", unit_price: " + tokens[5]);
            } else if (tokens[0].equals("add_credit")) {
                deliverySystem.addCredit(user.getAccountID(), Double.valueOf(tokens[1]));
            } else if (tokens[0].equals("display_order")) {
                deliverySystem.displayOrderOfStore(tokens[1],tokens[2]);
//                  System.out.println("orderID: " + tokens[2]);
//                System.out.println("store: " + tokens[1] + ", order: " + tokens[2]);
            }else if (tokens[0].equals("purchase_order")) {
                deliverySystem.purchaseOrder(tokens[1], tokens[2]);
//                  System.out.println("store: " + tokens[1] + ", order: " + tokens[2]);
            } else if (tokens[0].equals("cancel_order")) {
                deliverySystem.cancelOrder(tokens[1],tokens[2]);
//                  System.out.println("store: " + tokens[1] + ", order: " + tokens[2]);
            } else if (tokens[0].equals("display_customer_info")) {
                deliverySystem.displayCustomerInfo(user.getAccountID());
            } else if (tokens[0].equals("logout")) {
                System.out.println("stop acknowledged");
                break;
            } else {
                System.out.println("command " + tokens[0] + " NOT acknowledged");
             }
        }
    }


    private void adminOperations(User user, DeliverySystem deliverySystem) {
        Scanner commandLineInput = new Scanner(System.in);
        String wholeInputLine;
        String[] tokens;
        final String DELIMITER = ",";
        while (true) {
            wholeInputLine = commandLineInput.nextLine();
            tokens = wholeInputLine.split(DELIMITER);
            if (tokens[0].equals("make_store")) {
                deliverySystem.addStore(tokens[1], tokens[2]);
            } else if (tokens[0].equals("display_stores")) {
                deliverySystem.displayStore(user.getUserType());
            } else if (tokens[0].equals("remove_store")) {
                deliverySystem.removeStore(tokens[1]);
            } else if (tokens[0].equals("add_sell_item")) {
                deliverySystem.addSellItem(tokens[1],  tokens[2], Double.valueOf(tokens[3]),  Double.valueOf(tokens[4]));
            } else if (tokens[0].equals("update_item")) {
                deliverySystem.updateSellItem(tokens[1],  tokens[2], Double.valueOf(tokens[3]),  Double.valueOf(tokens[4]));
            } else if (tokens[0].equals("display_items")) {
                deliverySystem.displaySellItems(tokens[1]);
            } else if (tokens[0].equals("remove_item")) {
                deliverySystem.removeItem(tokens[1], tokens[2]);
            } else if (tokens[0].equals("make_pilot")) {
                deliverySystem.addPilot(tokens[1], tokens[2], tokens[3], tokens[4], tokens[5], tokens[6], tokens[7]);
            } else if (tokens[0].equals("display_pilots")) {
                deliverySystem.displayPilots();
            } else if (tokens[0].equals("remove_pilot")) {
                deliverySystem.removePilot(Integer.valueOf(tokens[1]));
            } else if (tokens[0].equals("make_drone")) {
                deliverySystem.addDrone(tokens[1], tokens[2], Double.valueOf(tokens[3]), Integer.valueOf(tokens[4]));
            } else if (tokens[0].equals("display_drones")) {
                deliverySystem.displayDrones(tokens[1]);
            } else if (tokens[0].equals("remove_drone")) {
                deliverySystem.removeDrone(tokens[1], tokens[2]);
            } else if (tokens[0].equals("make_customer")) {
                deliverySystem.addCustomer(tokens[1],tokens[2],tokens[3],tokens[4],tokens[5],tokens[6]);
//                    System.out.print("account: " + tokens[1] + ", first_name: " + tokens[2] + ", last_name: " + tokens[3]);
//                    System.out.println(", phone: " + tokens[4] + ", rating: " + tokens[5] + ", credit: " + tokens[6]);
            } else if (tokens[0].equals("display_customers")) {
                deliverySystem.displayCustomers();
            } else if (tokens[0].equals("remove_customer")) {
                deliverySystem.removeCustomer(Integer.valueOf(tokens[1]));
            } else if (tokens[0].equals("display_orders")) {
                deliverySystem.displayAllOrdersWithStoreName(tokens[1]);
//                    System.out.println("store: " + tokens[1]);
            } else if (tokens[0].equals("clean_historical_orders")) {
                deliverySystem.cleanHistoricalOrders();
            } else if (tokens[0].equals("logout")) {
                System.out.println("stop acknowledged");
                break;
            } else {
                System.out.println("command " + tokens[0] + " NOT acknowledged");
            }
        }

    }

}
