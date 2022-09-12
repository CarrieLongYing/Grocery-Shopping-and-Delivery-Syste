package service;

import entity.Pilot;
import java.util.List;

public interface PilotService {
    void addPilot(Pilot pilot);
    Pilot getPilotWithLicenseId(String licenseID);
    void addNumOfDeliveries(int pilotAccountID);
    Pilot findAvailablePilot();
    List<Pilot> getAllPilots();
    void freePilot(String droneID, int pilotAccountID);
    void removePilotWithAccountID(int accountID);
    Pilot getPilotWithAccountID(int accountID);
}
