package AddressParser;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.regex.Pattern;
import java.util.regex.Matcher;

public class Parser {

    private ArrayList<String> rawAddressList = new ArrayList<>();
    private ArrayList<String> cleanAddressList = new ArrayList<>();

    /**
     * The constructor of the Parser. It loads in the road_names.txt file, and
     * creates two arrays, a cleaned one and a raw one. The clean one is used
     * for comparing with user-input the other one is used for output. Here we
     * also sort the two collections.
     *
     * @throws FileNotFoundException throws it if there went something wrong
     * with reading the streetnames file.
     */
    public Parser() {
        try {
            BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream("road_names.txt"), "LATIN1"));
            StringBuilder sb = new StringBuilder();
            String line = br.readLine();

            while (line != null) {
                rawAddressList.add(line);
                cleanAddressList.add(cleanString(line));
                line = br.readLine();
            }
            br.close();
            Collections.sort(rawAddressList);
            Collections.sort(cleanAddressList);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Takes the input, and character by character broaden the streetnames in
     * the streetname file, to match the input. If there are 5 or less elements
     * that matches at any given point, then it tests if they are the wanted
     * element.
     *
     * @param input a string which has to be gone through to find an address.
     * @return
     * @throws InvalidInputException
     *
     */
    public String checkAddressExist(String input) throws InvalidInputException {

        int indexLow = 0;
        int indexHigh = cleanAddressList.size();
        for (int i = 0; i < input.length(); i++) {
            boolean firstAppereance = true;
            boolean lastAppereance = true;
            for (int j = indexLow; j < indexHigh; j++) {
                String s3 = cleanAddressList.get(j);
                if (s3.length() < i + 1) {
                    continue;
                }
                String s1 = s3.substring(0, i + 1);
                String s2 = input.substring(0, i + 1);
                if (s1.equals(s2) && firstAppereance == true) {
                    firstAppereance = false;
                    indexLow = j;
                }
                int counter = 0;
                do {
                    int charValue = s2.charAt(i);
                    String next = String.valueOf((char) (charValue + 1));
                    s2 = input.substring(0, i) + next;
                    counter++;
                } while (!s1.equals(s2) && counter < 75 && lastAppereance == true);
                if (s1.equals(s2) && lastAppereance == true) {
                    lastAppereance = false;
                    indexHigh = j;
                }
            }
            firstAppereance = true;
            lastAppereance = true;
            if (indexHigh - indexLow <= 5) {
                InvalidInputException ex = new InvalidInputException();
                for (int k = indexHigh; k >= indexLow; k--) {
                    if (input.length() >= cleanAddressList.get(k).length()) {
                        if (cleanAddressList.get(k).equals(input.substring(0, cleanAddressList.get(k).length()))) {
                            System.out.println("Best street name match: " + cleanAddressList.get(k));
                            return cleanAddressList.get(k);
                        }
                    }
                    ex.addPossibleStreetName(cleanAddressList.get(k));
                }
                throw ex;
            }
        }
        throw new InvalidInputException("No street matches");
    }

    /**
     *
     * @param input some String which needs to be cleaned
     * @return
     */
    public String cleanString(String input) {
        String cleanString;
        String dirtyString = input;
        for (SpecialChars s : SpecialChars.values()) {
            dirtyString = dirtyString.replaceAll(s.getChar(), "");
        }
        dirtyString = dirtyString.toLowerCase();
        cleanString = dirtyString;
        return cleanString;
    }

    /**
     * Takes a string and splits it up into the different blocks that an address
     * is made of and then return the array with the blocks. If the parsed
     * address does not contain some information, null will take the
     * corrosponding spot in the array.
     *
     * @param parseMe the address to parse. This must be a single address.
     * @return a String array with all the blocks an address is made of.
     * @throws ExceptionPackage.InvalidInputException
     */
    public String[] parseThis(String parseMe) throws InvalidInputException {
        // checks on the parseMe string
        if (parseMe.contains("?")) {
            throw new InvalidInputException("Address contains an ?");
        } else if (parseMe.contains("!")) {
            throw new InvalidInputException("Address contains an !");
        } else if (parseMe.length() < 3) {
            throw new InvalidInputException("The input is too short");
        } else if (parseMe.length() > 100) {
            throw new InvalidInputException("The input is too long");
        }

        String[] result = new String[6]; // the array to return

        // go through the expressions
        for (int i = 0; i < Regex.values().length; i++) /* 1 */ {
            // create a pattern with the expression.
            Pattern pattern = Pattern.compile(Regex.values()[i].getRegex(), Pattern.MULTILINE);

            // replace " i " with a " , " for the regEx to divide the different blocks.
            parseMe = parseMe.replace(" i ", " , ").trim()+" ";

            Matcher matcher = pattern.matcher(parseMe);

            // find matches if any in the parseMe String
            while (matcher.find()) /* 2 */ {
                // create a new string with the first matched find.
                String matchedString = parseMe.substring(matcher.start(), matcher.end());
                // remove nonwanted chars
                matchedString = matchedString.replace(",", "").trim();
                if (!matchedString.equals("")) /* 3 */ {
                    // add the string to the resultset on the corrosponding spot.
                    result[i] = matchedString.replace(".", "").trim();
                } else {
                    // ... unless it was an empty string
                    result[i] = null;
                }

                /**
                 * remove the first appearence of the found string so that the
                 * regEx will not find it again.
                 */
                parseMe = parseMe.trim();
                parseMe += " ";
                while (parseMe.substring(0, 1).equals(",")) {
                    parseMe = parseMe.replaceFirst(",", "").trim()+ " ";
                }
                parseMe = parseMe.replaceFirst(matchedString, "").trim();
                // break out and go on to the next regEx
                break;
            }

        }
        return result;
    }

    public String parseStreetAddress(String s) throws InvalidInputException {
        String input = s.toLowerCase();
        String cleanedInput = cleanString(s);
        String streetName = checkAddressExist(cleanedInput);
        String s3;
        // Remove the way the user has typed the address...
        if (streetName != null) {
            for (int i = 0; i < streetName.length(); i++) {
                input = input.replaceFirst(streetName.substring(i, i + 1), "");
            }
        }
        // ... and replace it with our version + a ","
        s3 = streetName + ", " + input;
        return parseSingleAdress(s3);
    }

    /**
     * Takes a String and then split it into blocks of information about an
     * address and return a string with those blocks divided by an # char
     *
     * @param address a string which may contain information about an address.
     * @return a string with address information splittet by an # char
     */
    public String parseSingleAdress(String address) {
        Parser parser = new Parser();
        String[] addressSplitted;
        try {
            addressSplitted = parser.parseThis(address);
        } catch (InvalidInputException ex) {
            return ex.getMessageString() + " - cause: " + ex.getCauseString();
        }
        String finalString = "";
        for (int i = 0; i < addressSplitted.length; i++) {
            if (addressSplitted[i] != null) {
                if (i == 3) {
                    if (addressSplitted[i].contains("st")) {
                        finalString += "0";
                    } else {
                        Pattern pattern = Pattern.compile("\\d*", Pattern.MULTILINE);
                        Matcher matcher = pattern.matcher(addressSplitted[i]);
                        while (matcher.find()) {
                            finalString += addressSplitted[i].substring(matcher.start(), matcher.end());
                        }
                    }
                } else {
                    finalString += addressSplitted[i];
                }
            }
            if (i != addressSplitted.length - 1) {
                finalString += "#";
            }
        }
        return finalString;
    }

}
