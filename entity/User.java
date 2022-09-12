package entity;

import org.mindrot.jbcrypt.BCrypt;

import javax.persistence.*;

import org.mindrot.jbcrypt.BCrypt;

import java.util.Objects;

@Entity
@Table(name = "user")
public class User {
    @Id
    @Column(name = "accountID", nullable = false)
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    protected int accountID;
    @Column(name = "first_name", nullable = false)
    protected String firstName;
    @Column(name = "last_name", nullable = false)
    protected String lastName;
    @Column(name = "phone_number", nullable = false)
    protected String phoneNumber;
    @Column(name = "user_type", nullable = false)
    protected int userType;
    @Column(name = "password", nullable = false)
    protected String password;
    @Column(name = "account", nullable = false)
    protected String account;

    public User(String firstName, String lastName, String phoneNumber, int userType, String password, String account) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.phoneNumber = phoneNumber;
        this.userType = userType;
        this.password = password;
        this.account = account;
    }

    // public User(String firstName, String lastName, String phoneNumber, int user_type, String password) {
    //     this.firstName = firstName;
    //     this.lastName = lastName;
    //     this.phoneNumber = phoneNumber;
    //     this.userType = user_type;
    //     this.password = password;
    // }

    public User() {}

    public int getAccountID() {
        return accountID;
    }

    public void setAccountID(int accountID) {
        this.accountID = accountID;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public int getUserType() {
        return userType;
    }

    public void setUserType(int userType) {
        this.userType = userType;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = BCrypt.hashpw(password, BCrypt.gensalt());;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }
}
