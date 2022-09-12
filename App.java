import entity.User;
import service.UserService;
import service.UserServiceImpl;

public class App {
    public static void main(String[] args) {
        System.out.println("Welcome to the Grocery Express Delivery Service!");
        System.out.println("login or signup");
        DeliveryService simulator = new DeliveryService();
        simulator.commandLoop();
    }
}
