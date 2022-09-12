package Dao;

import entity.Customer;
import entity.User;

import javax.persistence.*;
import java.util.List;

public class CustomerDao {
    static EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("db_connection");
    static EntityManager entityManager = entityManagerFactory.createEntityManager();
    public int addCustomer(Customer customer) {
        EntityTransaction et = null;
        int accountId = Integer.MIN_VALUE;
        try {
            // Get hibTransaction and start
            et = entityManager.getTransaction();
            et.begin();
            // Save the messageFrame object
            entityManager.persist(customer);
            System.out.println("customer accountID: " + customer.getAccountID());
            accountId = customer.getAccountID();
            et.commit();
        } catch (Exception ex) {
            // If there is an exception rollback changes
            if (et != null && et.isActive()) {
                et.rollback();
            }
            ex.printStackTrace();
            throw ex;
        }
        return accountId;
    }




    public List<Customer> getAllCustomers() {
        try {
            List<Customer> customers = entityManager.createQuery("select customer from Customer customer")
                    .getResultList();
            return customers;
        } catch (Exception ex) {
            ex.printStackTrace();
            throw ex;
        }
    }
    public void removeCustomerWithAccountID(int accountID) {
        EntityTransaction et = null;
        try {
            et = entityManager.getTransaction();
            et.begin();
            Query query = entityManager.createQuery("delete from Customer customer where accountID =: accountID");
            query.setParameter("accountID",accountID);
            query.executeUpdate();
            et.commit();
        } catch (Exception ex) {
            ex.printStackTrace();
            throw ex;
        }
    }

    public void decreaseCredit(int accountID, double orderCost) { // CZY: is it better to return the new customer credit?
        try {
            entityManager.getTransaction().begin();
            Query query = entityManager.createQuery("update Customer set credit = credit - :newCredit where accountID =:accountID")
            .setParameter("newCredit", orderCost)
            .setParameter("accountID", accountID);
            query.executeUpdate();
            // System.out.println(customer.getCredit());
            entityManager.getTransaction().commit();
        } catch (Exception ex) {
            ex.printStackTrace();
            throw ex;
        }
    }

    public Customer getCustomerWithAccountID(int accountID) {
        try {
            List<Customer> customers = entityManager.createQuery("select customer from Customer customer where accountID =:accountID")
                    .setParameter("accountID", accountID)
                    .getResultList();
            return customers.get(0);
        } catch (Exception ex) {
            ex.printStackTrace();
            throw ex;
        }
    }

    public void addCredit(int accountID, Double credit) {
        try {
            entityManager.getTransaction().begin();
            Query query = entityManager.createQuery("update Customer set credit = credit + :addedCredit where accountID=:accountID")
                    .setParameter("addedCredit", credit)
                    .setParameter("accountID",accountID);
            query.executeUpdate();
            entityManager.getTransaction().commit();
        } catch (Exception ex) {
            ex.printStackTrace();
            throw ex;
        }
    }
}
