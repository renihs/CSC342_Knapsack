package Program1.CSC342_Knapsack;

/**
 * @author Stephen Austin Shiner
 * Account #21
 * @author Bryan Beale
 * Account #5
 * Created at 1:23:35 PM on Aug 31, 2015 using UTF-8 encoding
 * Completed: September 11, 2015
 * File: p1v2.java
 * License: GNU GPLv3 
 * Purpose: CSC 342 Lab 1
 * Illustrate familiarity with/comprehension of designing a simple, exhaustive
 * algorithm for the NP-Hard Knapsack problem, as well as timing and profiling tools
 * and the statistical methods to make use of this measured data
 * 
 */

import java.io.File;
import java.io.IOException;
import java.util.Scanner;


public class p1v2
{
    public static void main(String[] args) throws IOException
    {
        //System.out.println("You entered: " + args[0]);
        if (args.length != 1 && args.length != 2) // Check for correct # of command-line arguments
        {   // Print failure message and display correct usage syntax
            System.out.println("Sorry, but you must enter either one or two command-"
                    + "line arguments.\n"
                    + "To use an input file, use a command like: java p1v2 ../instr/p1.dat\n"
                    + "To use randomly generated item values, but user-provided "
                    + "number of items and maximum size, use a command like: java p1v2 5 25");
            System.exit(0); // End program execution
        }
        
        String INPUT_FILE = ""; // Declare empty file name string
        
        // Declare the values to hold our nanosecond run times
        long startTime, stopTime;

        int n, w; // Declare our number and weight/size variables
        int[][] valuesAndSizes; // Declare our 2D arry which will hold the values and sizes
        int[][] optimal; // Will hold optimal subset values
        boolean[][] include; // Will hold whether or not to include the item at that weight/size combination
        
        if (args.length == 1) // Check if we have a command-line filename argument
        {
            INPUT_FILE = args[0]; // Assign command-line filename argument
        
            File file = new File(INPUT_FILE); // Create new File instance with input file name String

            // Verify input file's existence, and end program run if file not found
            if (!file.exists())
            {   // Print failure to open input file message
                System.out.println("Sorry, " + INPUT_FILE + " doesn't exist. "
                                        + "Program will now terminate. Goodbye.");
                System.exit(0); // End program run
            }

            // Establish program/file connection
            Scanner inputFile = new Scanner(file);

            String[] keyValues = inputFile.nextLine().split(" "); // Split up the key knapsack values as Strings
            n = Integer.parseInt(keyValues[0]); // Convert our number of values
            w = Integer.parseInt(keyValues[1]); // Convert our size limit value

            // Declare 2D array of size 2,n+1 to hold the pairs of values/sizes
            valuesAndSizes = new int[2][n + 1];
            // Declare two 2D arrays, one to hold maximum value with size limit w, possibly optimal,
            // the other to hold booleans for recording if the subset is optimal
            optimal = new int[n + 1][w + 1];
            include = new boolean[n + 1][w + 1];

            int sentinel = 1; // Initialize sentinel variable for value/size read in loop
            // Loop reads in values and sizes from the input file and stores them in a 2D array
            while (inputFile.hasNext() && sentinel <= n)
            {
                String[] valueSizePair = inputFile.nextLine().split(" "); // Get and split the value/size pair
                valuesAndSizes[0][sentinel] = Integer.parseInt(valueSizePair[0]); // Convert and store value
                valuesAndSizes[1][sentinel] = Integer.parseInt(valueSizePair[1]); // Convert and store size
                sentinel++; // Increment sentinel value by one
            }

            inputFile.close(); // Terminate program/file connection

        }
        else // We want to generate random value/size pairs and use command-line arguments as n and w
        {
            n = Integer.parseInt(args[0]); // Parse and store our number of items
            w = Integer.parseInt(args[1]); // Parse and store our maximum weight/size
            valuesAndSizes = new int[2][n + 1];
            // Declare two 2D arrays, one to hold maximum value with size limit w, possibly optimal,
            // the other to hold booleans for recording if the subset is optimal
            optimal = new int[n + 1][w + 1];
            include = new boolean[n + 1][w + 1];
            
            // Generate random value/size pairs for items 1 to n
            for (int i = 1; i <= n; i++)
            {
                valuesAndSizes[0][i] = (int) (Math.random() * 1000); // Generate random value between 0 and 1000
                valuesAndSizes[1][i] = (int) (Math.random() * w); // Generate random size between 0 and max weight
            }
        }
        
        // start timer for execution timing
        startTime = System.nanoTime();
        
        // This is the main set of nested loops
        // Loops will iterate through each set (and subset) of values exhaustively,
        // to determine optimal sets
        for (int i = 1; i <= n; i++)
        {
            for (int j = 1; j <= w; j++)
            {
                int setItem1 = optimal[i - 1][j]; // Never take the first one (ie 0)
                // Take item i for possible combo, first setting it to absolute min int
                int setItem2 = Integer.MIN_VALUE;
                if (valuesAndSizes[1][i] <= j)
                    setItem2 = valuesAndSizes[0][i] + optimal[i - 1][j - valuesAndSizes[1][i]];
                
                // Finally, we select the best of the two setItems
                optimal[i][j] = Math.max(setItem1, setItem2);
                if (setItem2 > setItem1) // Then set its flag
                    include[i][j] = true;
                else
                    include[i][j] = false;
            }
        }
        
        // Final optimal set of items
        boolean[] finalInclude = new boolean[n + 1];
        // Loop determines final optimal set of items
        for (int i = n, j = w; i > 0; i--)
        {
            if (include[i][j]) // If item is to be included, mark it and move to next set
            {
                finalInclude[i] = true;
                j = j - valuesAndSizes[1][i];
            }
            else // Item is not to be included, mark as false and move to next item
                finalInclude[i] = false;
        }
        
        stopTime = System.nanoTime(); // Get time algorithm completes
        long time = stopTime - startTime; // Calculate time to execute algorithm
        
        // Print out final results, formatted for 7-char left-justified columns
        System.out.printf("%-7s%-7s%-7s%-7s%n", "item #", "value", "size", "include");
        for (int i = 1; i <= n; i++)
            System.out.printf("%-7d%-7d%-7d%-7b%n", i, valuesAndSizes[0][i], valuesAndSizes[1][i], finalInclude[i]);
        
        System.out.println("Total Execution Time in nanoseconds: " + time); // Print time to execute algorithm in nanoseconds
        System.out.println("Total Execution Time in microseconds: " + time / 1000.0); // Print time to execute algorithm in microseconds
        System.out.println("Total Execution Time in milliseconds: " + time / 1000000.0); // Print time to execute algorithm in milliseconds
    }

}

