package service;

import Dao.CustomerDao;
import entity.Customer;
import java.util.List;
public class CustomerServiceImpl implements CustomerService{
    CustomerDao customerDao = new CustomerDao();
    @Override
    public void addCustomer(Customer customer) {
        customerDao.addCustomer(customer);
    }
    @Override
    public List<Customer> getAllCustomers() {
        return customerDao.getAllCustomers();
    }
    @Override
    public void removeCustomerWithAccountID(int accountID) {
        customerDao.removeCustomerWithAccountID(accountID);
    }
    public void decreaseCredit(int accountID, double orderCost){
        customerDao.decreaseCredit(accountID,orderCost);
    }

    @Override
    public Customer getCustomerWithAccountID(int accountID) {
        return customerDao.getCustomerWithAccountID(accountID);
    }

    public void addCredit(int accountID, Double credit) {
        customerDao.addCredit(accountID, credit);
    }
}
