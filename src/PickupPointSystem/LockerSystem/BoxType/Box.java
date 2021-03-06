package PickupPointSystem.LockerSystem.BoxType;

import PickupPointSystem.DatabaseSystem.Tables.PackageTable;
import PickupPointSystem.LockerSystem.Size;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

/**
 * This class represents the box
 * @author Andrea Stella
 * @version 1.0
 */

public abstract class Box  implements Comparable {

    protected Size size;
    private PackageTable pack;
    private int boxNumber;
    private static int numBox;

    /**
     * The constructor.
     */

    public Box(){
        numBox ++;
        boxNumber = numBox;
        pack = null;
    }

    /**
     * If the box is empty the availability is true, else false
     * @return the availability
     */

    public boolean isAvailable(){
        return pack == null;
    }

    /**
     * @return the box size
     */

    public Size getSize(){
        return size;
    }

    /**
     * @return the box number
     */

    public int getBoxNumber(){
        return boxNumber;
    }

    /**
     * @return the pack
     */

    public PackageTable getPack(){
        return pack;
    }

    /**
     * This method adds the pack inside the box
     * @param pack the pack
     */

    public void addPackage(PackageTable pack){
        this.pack = pack;
    }

    /**
     * This method removes the pack from the box
     */

    public void removePackage(){
        pack = null;
    }

    /**
     * This method compares this box with another passed as
     * an argument based on size
     * @param o the object with which to make the comparison
     * @return 0 if the two boxes are the same size
     *         1 if this box is bigger
     *         -1 if this box is smaller
     */

    public int compareTo(Object o){
        Box boxObject = (Box)o;
        return size.compareTo(boxObject.getSize());
    }

    /**
     * This method generate a password for the box. The password
     * is generated by appending the box number to 6 random letters
     * @return the password generated
     */
    
    public String generateBoxPassword() {
        String letters = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";
        String password = "";
        Random rand = new Random();

        for (int i = 0; i < 6; i++) {
            int n = rand.nextInt(51);
            char c = letters.charAt(n);
            password += c;
        }

        if (boxNumber < 10) password += "0";
        password += String.valueOf(boxNumber);

        return password;
    }

}
