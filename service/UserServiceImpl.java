package service;

import Dao.UserDao;
import entity.User;
import org.mindrot.jbcrypt.BCrypt;

public class UserServiceImpl implements UserService{
    private UserDao userDao = new UserDao();

    @Override
    public int addUser(User user) {
        return userDao.addUser(user);
    }

    @Override
    public User getUserByAccountIDAndPW(int accountID, String password) {
       User user = userDao.getUserByAccountID(accountID);
       if (user == null) {
          return null;
       }
        if (BCrypt.checkpw(password, user.getPassword())) {
            return user;
        }
        return null;
    }

    @Override
    public User getUserByAccount(String account) {
        return userDao.getUserByAccount(account);
    }

    @Override
    public User getUserByAccountID(int accountID) {
        return userDao.getUserByAccountID(accountID);
    }

    @Override
    public void removeUserWithAccountID(int accountID) {
        userDao.removeUserWithAccountID(accountID);
    }


}