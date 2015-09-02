package Program1;

/**
 * @author Stephen Austin Shiner
 * Account #21
 * Created at 1:23:35 PM on Aug 31, 2015 using UTF-8 encoding
 * Completed: 
 * File: p1.java
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


public class p1
{
    public static void main(String[] args) throws IOException
    {
        final String INPUT_FILE = "../p1.dat"; // Input file String
        
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
        
        // Now we can start declaring/initializing our variables, since the file exists
        int n, w; // Declare our number and weight variables
        
        String[] keyValues = inputFile.nextLine().split(" "); // Split up the key knapsack values as Strings
        n = Integer.parseInt(keyValues[0]); // Convert our number of values
        w = Integer.parseInt(keyValues[1]); // Convert our size limit value
        
        // Declare 2D array of size n,n to hold the pairs of values/sizes
        int[][] valuesAndSizes = new int[2][n + 1];
        // Declare two 2D arrays, one to hold maximum value with size limit w, possibly optimal,
        // the other to hold booleans for recording if the subset is optimal
        int[][] optimal = new int[n + 1][w + 1];
        boolean[][] include = new boolean[n + 1][w + 1];
        
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
        
        // Print out final results, formatted for 7-char left-justified columns
        System.out.printf("%-7s%-7s%-7s%-7s%n", "item #", "value", "size", "include");
        for (int i = 1; i <= n; i++)
            System.out.printf("%-7d%-7d%-7d%-7b%n", i, valuesAndSizes[0][i], valuesAndSizes[1][i], finalInclude[i]);
    }

}

