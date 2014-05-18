package AddressParser;

import java.io.*;

/**
 * Created by Christian on 12/02/14.
 */
public class AddressMatcher {

    private String output;
    public AddressMatcher(String input) {
        Parser p = new Parser();
        try {
            System.out.println("Input: " + input);
            System.out.println("");
            output = p.parseStreetAddress(input);
            System.out.println("");
            System.out.println("Output: " + output);
            System.out.println("");
        } catch (InvalidInputException ex) {
            ex.printPossibleStreetName();
            output = "Invalid address";
            System.out.println("");
        }
    }

    public String getOutput() {
        return output;
    }
    
    

    /**
     * The active main method of the program. Waits for the user to input some
     * text and prints the address information in the correct format. If
     * something went wrong, the exception information will be printed instead.
     *
     * @param args
     * @throws InvalidInputException
     */
    public static void main(String[] args) {
        AddressMatcher am = new AddressMatcher("Rued Langgaards Vej 9 københavn s");
    }
}
