package Dao;
import entity.User;

import javax.persistence.*;
import java.util.List;

public class UserDao {
    static EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("db_connection");
    static EntityManager entityManager = entityManagerFactory.createEntityManager();
    public int addUser(User user) {
        EntityTransaction et = null;
        int accountId = Integer.MIN_VALUE;
        try {
            // Get hibTransaction and start
            et = entityManager.getTransaction();
            et.begin();
            // Save the messageFrame object
            entityManager.persist(user);
            System.out.println("user accountID: " + user.getAccountID());
            accountId = user.getAccountID();
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

    public User getUserByAccountIDAndPW(int accountID, String password) {
        try {
            List<User> users = entityManager.createQuery("select user from User user where accountID =:accountID and password =:password")
                    .setParameter("accountID", accountID)
                    .setParameter("password", password)
                    .getResultList();
            if (users == null || users.size() == 0) {
                return null;
            }
            return users.get(0);
        } catch (Exception ex) {
            ex.printStackTrace();
            throw ex;
        }
    }

    public User getUserByAccount(String account) {
        try {
            List<User> users = entityManager.createQuery("select user from User user where account =:account")
                    .setParameter("account", account)
                    .getResultList();
            if (users == null || users.size() == 0) {
                return null;
            }
            return users.get(0);
        } catch (Exception ex) {
            ex.printStackTrace();
            throw ex;
        }
    }

    public User getUserByAccountID(int accountID) {
        try {
            List<User> users = entityManager.createQuery("select user from User user where accountID =:accountID")
                    .setParameter("accountID", accountID)
                    .getResultList();
            if (users == null || users.size() == 0) {
                return null;
            }
            return users.get(0);
        } catch (Exception ex) {
            ex.printStackTrace();
            throw ex;
        }
    }

    public void removeUserWithAccountID(int accountID) {
        EntityTransaction et = null;
        try {
            et = entityManager.getTransaction();
            et.begin();
            Query query = entityManager.createQuery("delete from User user where accountID =: accountID");
            query.setParameter("accountID",accountID);
            query.executeUpdate();
            et.commit();
        } catch (Exception ex) {
            ex.printStackTrace();
            throw ex;
        }
    }
}
