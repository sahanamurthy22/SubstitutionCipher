/**
 * User is given three options: encode, decode, or quit the program. For encode, translates into encoded language, usually with a positive shift. For decode, translates back into english, usually with a negative shift. Program creates a new file if doesn't already exists, and prints encoded or decoded message into a new file, given a message from a provided file. There is also a shift method, which takes into account the message and shift number, accounting for wraparounds and just creating the translation in general.
 * @author Sahana Murthy
 * @version 11/18/2024
 */
import java.util.Scanner;
import java.io.File;
import java.io.PrintWriter; 
import java.io.FileNotFoundException; 
import java.io.*;

public class SubstitutionCipher {

    /**
     * Private constants used to shift characters for the substitution cipher.
     */
    private static final String UPPERCASE = "ABCDEFGHIJKLMNOPQRSTUVWXYZ"; // accounting for uppercase chars
    private static final String LOWERCASE = "abcdefghijklmnopqrstuvwxyz"; // accounting for lowercase chars

    /**
     * Constructs a new String where each letter in the String input is shifted
     * by the amount shift to the right, preserving whether the original
     * character was uppercase or lowercase. For example, the String "ABC" with
     * shift 3 would cause this method to return "DEF". A negative value should
     * shift to the left. For example, the String "ABC" with shift -3 would
     * cause this method to return "XYZ". Punctuation, numbers, whitespace and
     * other non-letter characters should be left unchanged. NOTE: For full
     * credit you are REQUIRED to use a StringBuilder to build the String in
     * this method rather than using String concatenation.
     *
     * @param input
     *            String to be encrypted
     * @param shift
     *            Amount to shift each character of input to the right
     * @return the encrypted String as outlined above
     */
    public static String shift(String input, int shift) {
       StringBuilder word = new StringBuilder();
       int originalPosition = 0;
       int newPosition = 0;
       for (int i = 0; i < input.length(); i++) {
          boolean notLetter = false;
          if (Character.isWhitespace(input.charAt(i))|| !Character.isLetter(input.charAt(i))) { // if char is a blank or anything that isn't a letter automattically append to word
             word.append(input.charAt(i));
             notLetter = true; // if the char is a letter, then the boolean is set to true so that if statement below doesn't run
          }
          if (!notLetter) { // this if statement only runs if the char is a letter
            if (Character.isUpperCase(input.charAt(i))) { // for uppercase chars
               originalPosition = UPPERCASE.indexOf(input.charAt(i)); // index of char in the UPPERCASE string
               if (shift > 0 && (shift + originalPosition) > 25) { // if the shift makes index go above 25 then we need to accomodate for a wraparound
                  newPosition = shift - (25 - originalPosition) - 1; // wraps around to the start of the UPPERCASE string, newPosition is the index of the new char
                  word.append(UPPERCASE.charAt(newPosition)); // adds the new char to word
               } else if (shift < 0 && (shift + originalPosition) < 0) { // if shift makes index go below 0 we need to accomodate for a wraparound
                 newPosition = (originalPosition + shift) + 26;  // wraps around to the end of the UPPERCASE string
                 word.append(UPPERCASE.charAt(newPosition)); // adds the new char to word
               } else { // if the shift doesn't go above 25 or below 0, then we don't have to accomodate for wraparounds
                 newPosition = originalPosition + shift;
                 word.append(UPPERCASE.charAt(newPosition));
               }
            } else if (Character.isLowerCase(input.charAt(i))) { // same process for lowercase, the only difference for this else if statement is that the char is lowercase so we need to accomodate for that
               originalPosition = LOWERCASE.indexOf(input.charAt(i));
               if (shift > 0 && (shift + originalPosition) > 25) {
                  newPosition = shift - (25 - originalPosition) - 1;
                  word.append(LOWERCASE.charAt(newPosition));
               } else if (shift < 0 && (shift + originalPosition) < 0) {
                 newPosition = (originalPosition + shift) + 26; 
                 word.append(LOWERCASE.charAt(newPosition));
               } else {
                 newPosition = originalPosition + shift;
                 word.append(LOWERCASE.charAt(newPosition));
               }   
             }   
          }
      }
      return word.toString();  // turns word from type StringBuilder to type String
    }

    /**
     * Displays the message "promptMsg" to the user and reads the next full line
     * that the user enters. If the user enters an empty string, reports the
     * error message "ERROR! Empty Input Not Allowed!" and then loops,
     * repeatedly prompting them with "promptMsg" to enter a new string until
     * the user enters a non-empty String
     *
     * @param in
     *            Scanner to read user input from
     * @param promptMsg
     *            Message to display to user to prompt them for input
     * @return the String entered by the user
     */
    public static String promptForString(Scanner in, String promptMsg) {
       String response = "";
       while (response.equals("")) { // program keeps prompting if user keeps giving a blank message, while loop doesn't end until user enters a message that isn't blank
         System.out.print(promptMsg);
         response = in.nextLine();
         if (response.equals("")) {
            System.out.println("ERROR! Empty Input Not Allowed!");   
         }
      }
      return response; 
    }

    /**
     * Opens the file inFile for reading and the file outFile for writing,
     * reading one line at a time from inFile, shifting it the number of
     * characters given by "shift" and writing that line to outFile. If an
     * exception occurs, must report the error message: "ERROR! File inFile not
     * found or cannot write to outFile" where "inFile" and "outFile" are the
     * filenames given as parameters.
     *
     * @param inFile
     *            the file to be transformed
     * @param outFile
     *            the file to write the transformed output to
     * @param shift
     *            the amount to shift the characters from inFile by
     * @return false if an exception occurs and the error message is written,
     *         otherwise true
     */
    public static boolean transformFile(String inFile, String outFile,
            int shift) {
        Scanner scanner = null; // initialized so that it can be accessed outside of the try {} segment
        PrintWriter writer = null; // initialized so that it can be accessed outside of the try {} segment
        try {
           File outFileDir = new File(outFile).getParentFile();
           if (outFileDir != null && !outFileDir.exists()) {
            outFileDir.mkdirs();  
           } // creates a new file named the same as the outFile input if the outFile doesn't already exist
           
           scanner = new Scanner(new File(inFile)); // initializes
           writer = new PrintWriter(new File(outFile)); // initializes
           
           boolean isEncoding = shift > 0; // if encoding, we need a new line, so boolean is necessary to differentiate between encoding and decoding
           while (scanner.hasNextLine()) { 
              String line = scanner.nextLine();
              String shiftLine = shift(line,shift); // calls the shift method to produce the encoded or decoded message
              if (scanner.hasNextLine()) { 
                 writer.println(shiftLine); // only println if there is another line after
              } else {
                 if (isEncoding) {
                    writer.println(shiftLine);  // For encoding, include the final newline
                 } else {
                    writer.print(shiftLine);  // For decoding, no newline after the last line
                 }
              }
           }
           return true; // files exist so true
        } catch (FileNotFoundException e) { // if inFile doesn't already exist, then error
           System.out.println("ERROR! File " + inFile + " not found or cannot write to " + outFile);
           return false; // inFile doesn't exist so must return false
        } finally {
           if (scanner != null) {
              scanner.close(); // close the scanner so that printing to outFile works
           }
           if (writer != null) {
              writer.close(); // close the PrintWriter so that printing to outFile works
           }
           
        }
        
    }

    /**
     * Prompts the user to enter a single character choice. The only allowable
     * values are 'E', 'D' or 'Q'. All other values are invalid, including all
     * values longer than one character in length, however the user is allowed
     * to enter values in either lower or upper case. If the user enters an
     * invalid value, the method displays the error message "ERROR! Enter a
     * valid value!" and then prompts the user repeatedly until a valid value is
     * entered. Returns a single uppercase character representing the user's
     * choice.
     *
     * @param in
     *            Scanner to read user choices from
     * @return the user's choice as an uppercase character
     */
    public static char getChoice(Scanner in) {
        char letter = '\0'; // initalizing to null
        boolean valid = false; // initialized to false so while loop runs at least once
        
        while (!valid) { // while boolean false
           System.out.print("Enter your choice: ");
           String choice = in.nextLine().toUpperCase();
           if (choice.equals("E") || choice.equals("D") || choice.equals("Q")) {
              letter = choice.charAt(0);
              valid = true;
           } else {
              System.out.println("ERROR! Enter a valid value!"); // while loop keeps repeating until user enters valid value
           }
        }
        return letter;
    }

    /**
     * Displays the menu of choices to the user.
     */
    // listing out choices to the user
    public static void displayMenu() {
        System.out.println("[E]ncode a file");
        System.out.println("[D]ecode a file");
        System.out.println("[Q]uit");
    }

    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);
        displayMenu();
        char choice = getChoice(in);
        while (choice != 'Q') { // if user chooses 'Q' then program quits
           String inFile = promptForString(in, "Enter an input file: "); // prompts until user enters something that isn't blank
           String outFile = promptForString(in, "Enter an output file: "); // same thing here
           int shift = Integer.parseInt(promptForString(in, "Enter a shift amount: ")); // Integer.parseInt() converts from type string that method returns to type int
           if (choice == 'E') {
              transformFile(inFile, outFile, shift); // for encoding shift is positive
           } else if (choice == 'D') {
              transformFile(inFile, outFile, (-1 * shift)); // for decoding shift needs to be negative
           }
           System.out.println("Finished writing to file.");
           System.out.println();

           displayMenu(); // Show menu again after file processing
           choice = getChoice(in);
        }

        System.out.println();
        System.out.println("Goodbye!"); // goodbye if user hits 'Q'

        in.close(); // closes scanner to avoid bugs
    }

}
