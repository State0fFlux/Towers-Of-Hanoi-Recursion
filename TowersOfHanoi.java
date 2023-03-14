/*
 * This program will play multiple games of Towers of Hanoi with the user,
 * showing the user the solutions to varying Towers of Hanoi puzzles upon request,
 * and saving each solution to a .txt file upon request.
 * @author Brady Manske
 * @version 2023/03/17
 */
import java.util.*; // for Stack
import java.io.*; // for PrintStream

public class TowersOfHanoi {
    private static int towerHeight = 0;
    
    public static void main(String[] args) {
        // initializing objects & variables
        Scanner input = new Scanner(System.in);
        Peg pegA = new Peg('A');
        Peg pegB = new Peg('B');
        Peg pegC = new Peg('C');
        Peg[] pegs = {pegA, pegB, pegC};
        play(pegs, input);
        System.out.println("\nGoodbye!");
    } // end of main method
    
    /*
     * Pre: None
     * Post:
     * - Creates multiple Towers of Hanoi with the user,
     *   from user input gathered by given Scanner
     * - Solves puzzle using given pegs, and relays
     *   solution to the user
     * - Gives the user the option to save their solution
     *   or try again with a different tower
     */
    public static void play(Peg[] pegs, Scanner input) {
        setHeight(input);
        intro();
        solve(pegs, System.out);
        save(pegs, input);
        System.out.print("Would you like to solve another Towers of Hanoi puzzle? ");
        if (userSaysYes(input)) { // recursive case, user wants to play again
            System.out.println();
            play(pegs, input);
        } // end of if
    } // end of play method
    
    /*
     * Pre: None
     * Post:
     * - Acquires a value of at least 1 from the user
     * - Sets the tower height field to given integer
     */
    public static void setHeight(Scanner input) {
        // initializing variable
        char command = '!';
        
        System.out.println("How many disks would you like for your " +
                "Towers of Hanoi? [?] for help");
        try {
            towerHeight = input.nextInt();
            if (towerHeight < 1) { // recursive case, specified height is too short
                System.out.println("You must have at least one disk " +
                    "for this puzzle.");
                setHeight(input);
            } // end of if
        } catch (Exception e) { // recursive case, user entered a non-number
            command = input.nextLine().charAt(0);
            if (command == '?') { // user needs help
                towerHeight = 0;
                intro();
            } else { // user's input was simply invalid
                System.out.println("Please enter a number.");
            } // end of if/else
            setHeight(input);
        } // end of try/catch
    } // end of setHeight method
    
    /*
     * Pre: None
     * Post:
     * - Introduces the puzzle & its rules to the user
     */
    public static void intro() {
    // initializing objects
    String plural = "";
    String height = towerHeight + "";
    
    if (towerHeight != 1) {
    // grammar fix, needs a plural suffix
        plural = "s";
    } // end of if
    if (towerHeight < 1) {
    // height is undecided
        height = "a number of";
    } // end of if
    System.out.println("\tThe Towers of Hanoi present themselves " +
        "as three diamond pegs.\nOne of the pegs has " + height +
        " golden disk" + plural + " stacked on top of it.\nThese disks decrease in size, " +
        "with the largest disks\nat the bottom of peg, and the smallest " +
        "at the top.\n\n\tYour goal is to move every disk from this peg " +
        "onto another peg.\nUnfortunately, only one disk can be moved " +
        "at a time,\nand a disk can only be placed either on an empty " +
        "peg,\nor atop a larger disk.\n");
    } // end of intro method
    
    /*
     * Pre: None
     * Post:
     * - Solves a single Towers of Hanoi puzzle using given pegs
     * - Prints solution to given output location
     */
    public static void solve(Peg[] pegs, PrintStream output) {
        String plural = "";
        reset(pegs);
        if (towerHeight > 1) { // grammar fix, needs a plural suffix
            plural = "s";
        } // end of if
        output.println("Here is how to solve a " + towerHeight + "-disk tall Towers " +
            "of Hanoi puzzle in " + (int)(Math.pow(2, towerHeight) - 1) + " step" + plural + ":");
        printPegs(pegs, output);
        move(towerHeight, pegs[0], pegs[2], pegs[1], pegs, output);
        output.println();
    } // end of solve method
    
    /*
     * Pre: None
     * Post:
     * - Resets the Towers of Hanoi so that all the disks are stacked on peg A, decreasing in size from bottom to top, and pegs B and C are empty
     */
    public static void reset(Peg[] pegs) {
        // clearing pegs
        pegs[0].clear();
        pegs[1].clear();
        pegs[2].clear();
        
        for (int i = towerHeight; i > 0; i--) { // stocking peg A
            pegs[0].push(i);
        } // end of for loop
    } // end of reset method
    
    /*
     * Pre: None
     * Post:
     * - Solves the Towers of Hanoi puzzle for given Peg setup
     * - Prints step-by-step instructions, and a visual representation
     *   of the puzzle-solving process, to the given output location
     */
    public static void move(int n, Peg giver, Peg receiver,
        Peg storage, Peg[] pegs, PrintStream output) {
        if (n > 0) { // recursive case
            move(n-1, giver, storage, receiver, pegs, output);
            output.println("\nMove disk " + n + " from rod " + 
                giver.getName() + " to rod " + receiver.getName() + ":");
            receiver.push(giver.pop());
            printPegs(pegs, output);
            move(n-1, storage, receiver, giver, pegs, output);
        } // end of if
    } // end of move method
    
    /*
     * Pre: None
     * Post:
     * - Prints the given pegs in order
     * - Prints a visual representation of the disks
     *   on each given peg to the given output location
     */
    public static void printPegs(Peg[] pegs, PrintStream output) {
        // initializing variable
        int maxHeight = 0;
        // updating maxHeight
        for (Peg peg : pegs) { // repeats for pegs A, B, and C
            if (peg.size() > maxHeight) {
            // max height is outdated
                maxHeight = peg.size(); // updates value
            } // end of if
        } // end of for each loop
        
        for (int i = maxHeight - 1; i >= 0; i--) {
        // repeats for every layer of the tallest tower,
        // building from top to bottom
            for (Peg peg : pegs) { // repeats for each peg
                String disk = "";
                try {
                    disk = peg.diskToString(i);
                } catch (ArrayIndexOutOfBoundsException e) {
                // there is no disk on this layer of this peg
                    disk = "";
                } // end of try/catch
                output.printf("%" + towerHeight + "s", disk);
                output.print("   ");
            } // end of for each loop
            output.println();
        } // end of for loop
        for (Peg peg : pegs) { // labels each peg
            printCentered("" + peg.getName(), towerHeight, output);
            output.print("   ");
        } // end of for each loop
        output.println();
    } // end of printPegs method
    
    /*
     * Pre: None
     * Post:
     * - Centers the given String in the given space size
     * - Prints centered String to the given output
     */
    public static void printCentered(String str, int space, PrintStream output) {
        output.printf("%" +
            (space -((space - str.length()) / 2)) + "s", str);
        for (int i = 0; i < ((space - str.length()) / 2); i++) {
            output.print(" ");
        } // end of for loop
    } // end of printCentered method
    
    /*
     * Pre: None
     * Post:
     * - Acquires a [y]es/[n]o answer from the user
     * - Returns a boolean representing the user's response
         (yeah -> true, nah -> false)
     */
    public static boolean userSaysYes(Scanner input) {
        char response = '!';
        try {
            response = input.nextLine().charAt(0);
        } catch (StringIndexOutOfBoundsException e) { // user entered a non-character (hit enter)
            response = '_';
        } // end of try/catch
        if (response == 'y') {
            return true;
        } else if (response == 'n') {
            return false;
        } else { // recursive case, user did not give a [y]es/[n]o answer
            if (response != '_') { // user gave an invalid response, not just hitting enter
                System.out.println("Please respond with [y]es or [n]o.");
            } // end of if
            return userSaysYes(input);
        } // end of if/elses
    } // end of userSaysYes method
    
    /*
     * Pre: User must say yes to prompt
     * Post:
     * - Acquires a .txt file from the user via given scanner
     * - Prints the current Towers of Hanoi solution to the .txt file
     * - Ensures that the user is okay with overwriting, if acquired .txt file already exists
     * - Does nothing if user says no to prompt
     */
    public static void save(Peg[] pegs, Scanner input) {
        // initializing variable
        boolean consent = true;
        
        System.out.print("Would you like to save this solution? ");
        if (userSaysYes(input)) { // user wants to print solution to file
            System.out.print("Please enter the name of the file you'd like to save to: ");
            String fileName = input.nextLine();
            if (fileName.equals("")) { // recursive case, user failed to provide a name
                fileName = "/"; // allows for error to be caught in later code
            } // end of if
            if (!fileName.endsWith(".txt")) { // safety precaution, ensures it will be a text file
                fileName += ".txt";
            } // end of if
            if (new File(fileName).exists()) { // file exists, may overwrite existing info
                System.out.print("This file already exists. Are you okay with overwriting " + fileName + "? ");
                if (!userSaysYes(input)) { // user does not consent to overwriting file
                    consent = false;
                    System.out.println("Okay. Please try again with a new file name.");
                    save(pegs, input);
                } // end of if
            } // end of if
            if (consent) { // user still consents to saving file
                try {
                    solve(pegs, new PrintStream(fileName));
                    System.out.println("\n- solution successfully saved as " + fileName + " -\n");
                } catch (FileNotFoundException e) { // a file with this name could not be created
                    System.out.println("File not found. Please try again.");
                    save(pegs, input);
                } // end of try/catch
            } // end of if
        } // end of  if
    } // end of save method
} // end of TowersOfHanoi class