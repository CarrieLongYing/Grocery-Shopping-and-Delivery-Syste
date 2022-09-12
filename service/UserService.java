package service;

import entity.User;

public interface UserService {
    int addUser(User user);
    User getUserByAccountIDAndPW(int accountID, String password);
    User getUserByAccount(String account);
    User getUserByAccountID(int accountID);
    void removeUserWithAccountID(int accountID);
}
