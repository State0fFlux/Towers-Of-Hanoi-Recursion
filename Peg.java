/*
 * This defines a Peg class that extends the Stack class
 * by adding to it the ability to store a character
 * representing its "name" or label.
 * @author Brady Manske
 * @version 2023/03/17
 */
import java.util.*; // for Stack

public class Peg extends Stack<Integer> {
    // declaring private field
    private char name;
    
    /*
     * - Constructs an empty Peg storing given label
     */
    public Peg(char name) {
        this.name = name;
    } // end of constructor
    
    /*
     * - Returns the Peg's label
     */
    public char getName() {
        return name;
    } // end of getName method
    
    /*
     * - Returns a String representing the disk stored on the Peg at given index
     */
    public String diskToString(int index) {
        String disk = "";
        int width = this.get(index);
        for (int i = 0; i < width; i++) {
            disk += "-";
        }
        return disk;
    } // end of diskToString method
} // end of Peg class
