package service;

import Dao.PilotDao;
import entity.Pilot;
import java.util.List;

public class PilotServiceImpl implements PilotService{
    PilotDao pilotDao = new PilotDao();
    @Override
    public void addPilot(Pilot pilot) {
        pilotDao.addPilot(pilot);
    }

    @Override
    public Pilot getPilotWithLicenseId(String licenseID) {
        return pilotDao. getPilotWithLicenseId(licenseID);
    }

    @Override
    public void addNumOfDeliveries(int pilotAccountID){
        pilotDao.addNumOfDeliveries(pilotAccountID);
    }

    @Override
    public Pilot findAvailablePilot(){
        return pilotDao.findAvailablePilot();
    }

    @Override
    public List<Pilot> getAllPilots() {
        return pilotDao.getAllPilots();
    }

    @Override
    public void removePilotWithAccountID(int accountID) {
        pilotDao.removePilotWithAccountID(accountID);
    }

    @Override
    public Pilot getPilotWithAccountID(int accountID) {
        return pilotDao.getPilotWithAccountID(accountID);
    }

    @Override
    public void freePilot(String currDroneId, int pilotAccountID){pilotDao.freePilot(currDroneId,pilotAccountID);}
}
