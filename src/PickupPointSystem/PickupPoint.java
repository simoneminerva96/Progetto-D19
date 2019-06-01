package PickupPointSystem;

import PickupPointSystem.DatabaseSystem.PersistenceFacade;
import PickupPointSystem.DatabaseSystem.Tables.Delivery;
import PickupPointSystem.GraphicalInterface.ErrorGUI.ErrorGUIMain;
import PickupPointSystem.GraphicalInterface.GraIntMain;
import PickupPointSystem.LockerSystem.BoxType.*;
import PickupPointSystem.DatabaseSystem.Tables.Package;

import PickupPointSystem.LockerSystem.Size;
import ObserverPattern.Observer;
import PickupPointSystem.Server.PickupPointServer;

import java.io.IOException;
import java.util.*;

/**
 * @author Andrea Stella
 * @version 1.0
 */


public class PickupPoint {

    private static PickupPoint instance = null;
    private String id;
    private ArrayList<Box> boxList = new ArrayList<>();
    private HashMap<String, Box> availableBox = new HashMap<>();
    private ArrayList<Observer> obsList = new ArrayList<>();
    private PersistenceFacade facade = new PersistenceFacade();

    /**
     * The constructor.Adds the boxes to the list, updates the boxes from the database,
     * creates the server and the graphical interface.
     * @param id
     * @param numSmallBox
     * @param numMediumBox
     * @param numLargeBox
     * @throws IOException
     */

    private PickupPoint(String id, int numSmallBox, int numMediumBox, int numLargeBox) throws IOException {
        this.id = id;
        for(int i = 0; i < numSmallBox; i++){
            boxList.add(new SmallBox());
        }
        for(int i = 0; i < numMediumBox; i++){
            boxList.add(new MediumBox());
        }
        for(int i = 0; i < numLargeBox; i++){
            boxList.add(new LargeBox());
        }
        updateBox();
        createServer();
        createGUI();
    }

    public static PickupPoint getInstance(String id, int numSmallBox, int numMediumBox, int numLargeBox) throws IOException {
        if(instance==null){
            instance = new PickupPoint(id, numSmallBox, numMediumBox, numLargeBox);
        }
        return instance;
    }

    /**
     * This method adds the package to a specific box, adds the box to the available boxes
     * by setting the password and updates the database delivery.
     * @param pack
     * @return boxNumber
     * @throws IOException
     */

    public int addPackage(Package pack) throws IOException {
        Collections.sort(boxList);
        for(Box box : boxList){
            if(box.isAvailable() && box.getSize().compareTo(pack.getSize()) > -1){
                box.addPackage(pack);
                Date dateOfDelivery = new Date();
                box.setDate(dateOfDelivery);
                String password = box.generateBoxPassword();
                availableBox.put(password, box);
                Delivery delivery = facade.getDeliveryFromPackID(id, pack.getId());
                delivery.setDateOfDelivery(dateOfDelivery);
                delivery.setBoxNumber(box.getBoxNumber());
                delivery.setBoxPassword(password);
                facade.updateDelivery(delivery);
                notifyObservers();
                return box.getBoxNumber();
            }
        }
        return 0;
    }

    /**
     * This method empties the box associated with the entered password and remove it from
     * the availableBox, remove the package and delivery from the database.
     * @param cod
     */

    public void emptyBox(String cod){
        Box box = null;
        Package pack = null;
        try {
            box = availableBox.get(cod);
            pack = box.getPack();
            box.removePackage();
            availableBox.remove(cod);
            facade.removeDelivery(pack.getId());
            facade.removePack(pack.getId());
            notifyObservers();
        }
        catch (NullPointerException e) {
            e.printStackTrace();
            new ErrorGUIMain("the code is invalid", false);
        }
    }

    /**
     * This method updates the status of the boxes from the database.
     */

    public void updateBox(){
        for(Box box : boxList){
            Delivery delivery = facade.getDeliveryFromBoxNumber(id, box.getBoxNumber());
            if(delivery!=null){
                box.addPackage(facade.getPackage(delivery.getPackID()));
                box.setDate(delivery.getDateOfDelivery());
                availableBox.put(delivery.getBoxPassword(),box);
            }
        }
    }

    public ArrayList<Box> getBoxList() {
        return boxList;
    }

    public Box getBoxFromIndex(int index) {
        return boxList.get(index);
    }

    public Size getBoxSizeGivenIndex(int index) {
        return getBoxFromIndex(index).getSize();
    }

    public String getId(){
        return id;
    }

    private void createServer() throws IOException {
        PickupPointServer server = new PickupPointServer();
        server.start();
    }

    private void createGUI() {
        GraIntMain gui = new GraIntMain(this);
        addObserver(gui);
    }

    private void addObserver(Observer obs) {
        for (Observer obsOfList : obsList) {
            if (obs == obsOfList) return;
        }

        obsList.add(obs);
    }

    private void notifyObservers(){
        for (Observer obsOfList : obsList) {
            obsOfList.update();
        }
    }
}
