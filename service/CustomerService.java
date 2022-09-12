package service;

import entity.Customer;

import java.util.List;

public interface CustomerService {
    void addCustomer(Customer customer);
    List<Customer> getAllCustomers();
    void removeCustomerWithAccountID(int accountID);
    void decreaseCredit(int accountID, double orderCost);
    Customer getCustomerWithAccountID(int accountID);
}
