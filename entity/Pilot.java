package entity;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "pilot")
public class Pilot {
//    > make_pilot,ffig8,Finneas,Fig,888-888-8888,890-12-3456,panam_10,33
    @Column(name = "taxID", nullable = false)
    private String taxId;
    @Column(name = "licenseID", nullable = false)
    private String licenseId;
    @Column(name = "deliveries", nullable = true)
    private int numOfDeliveries;
    @Column(name = "current_droneID", nullable = true)
    private String droneId;
    @Id
    @Column(name = "accountID", nullable = false)
    private int accountID;

    public Pilot(int accountID, String taxId, String licenseId, int numOfDeliveries) {
        this.accountID = accountID;
        this.taxId = taxId;
        this.licenseId = licenseId;
        this.numOfDeliveries = numOfDeliveries;
    }

    public Pilot() {

    }

    public void addNumOfDeliveries() {
        this.numOfDeliveries++;
    }

    public String getDroneId() {
        return droneId;
    }

    public void setDroneId(String droneId) {
        this.droneId = droneId;
    }

    public int getAccountID() {
        return accountID;
    }


    public void setAccountID(int accountID) {
        this.accountID = accountID;
    }

    public String getTaxId() {
        return taxId;
    }

    public void setTaxId(String taxId) {
        this.taxId = taxId;
    }

    public String getLicenseId() {
        return licenseId;
    }

    public void setLicenseId(String licenseId) {
        this.licenseId = licenseId;
    }

    public int getNumOfDeliveries() {
        return numOfDeliveries;
    }

    public void setNumOfDeliveries(int numOfDeliveries) {
        this.numOfDeliveries = numOfDeliveries;
    }

    @Override
    public String toString() {
        return "Pilot{" +
                "taxId='" + taxId + '\'' +
                ", licenseId='" + licenseId + '\'' +
                ", numOfDeliveries=" + numOfDeliveries +
                ", droneId='" + droneId + '\'' +
                ", accountID=" + accountID +
                '}';
    }
}
