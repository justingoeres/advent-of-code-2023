package org.jgoeres.adventofcode2023.Day13;

import org.jgoeres.adventofcode.common.XYPoint;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;

public class Day13Service {
    public boolean DEBUG = false;

    private ArrayList<Valley> valleys = new ArrayList<>();

    public Day13Service(String pathToFile) {
        loadInputs(pathToFile);
    }

    public Day13Service(String pathToFile, boolean debug) {
        loadInputs(pathToFile);
        DEBUG = debug;
    }

    public long doPartA() {
        System.out.println("=== DAY 13A ===");

        long result = 0;
        /**
         * Find the line of reflection in each of the patterns in your notes.
         * What number do you get after summarizing all of your notes?
         *
         * add up the number of columns to the left of each vertical line of reflection;
         * to that, also add 100 multiplied by the number of rows above each horizontal line of reflection.
         **/

        // Find vertical reflections
        for (Valley valley : valleys) {
            System.out.println("rows");
            Integer rowFoundAt = searchValues(valley.getRowValues());
            System.out.println("columns");
            Integer colFoundAt = searchValues(valley.getColValues());
            Integer valleyResult = colFoundAt + 100 * rowFoundAt;
            System.out.println("valley result:\t" + valleyResult);
            System.out.println();
            result += valleyResult;
        }

        System.out.println("Day 13A: Answer = " + result);
        return result;
    }

    private static Integer searchValues(List<Long> values) {
        int i = 0;
        while ((i + 1) < values.size()) {    // while we're in bounds
            // The first reflected column/row is the one that
            // is equal to its next neighbor
            if (values.get(i).equals(values.get(i + 1))) {
                int foundAt = i;
                System.out.printf("candidate found at:\t%d\n", foundAt);

                final Boolean isReflection = checkForMatches(foundAt, values);
                if (isReflection) {
                    // If this is a reflection, return the result
                    System.out.printf("reflection found at index %d\n", foundAt);
                    return foundAt + 1; // because the puzzle counts from 1 not 0 }
                }
            }
            i++;
        }
        // If we get here, we didn't find any reflections
        System.out.printf("Nothing found.\n");
        return 0;
    }

    private static Boolean checkForMatches(int foundAt, List<Long> values) {
        // Now that we found a candidate, check backwards to verify the match
        int side1 = foundAt;
        int side2 = foundAt + 1;
        while (side1 >= 0 && side2 < values.size()) {
            // as long as we're in bounds
            if (!values.get(side1).equals(values.get(side2))) {
                // If the two values are NOT equal
                System.out.printf("not a reflection at indexes %d and %d\n", side1, side2);
                return false;
            }
            side1--;
            side2++;
        }
        // If we get here, we're done checking and haven't failed, so this is a reflection.
        return true;
    }

    public long doPartB() {
        System.out.println("=== DAY 13B ===");

        long result = 0;
        /** Put problem implementation here **/

        System.out.println("Day 13B: Answer = " + result);
        return result;
    }

    // load inputs line-by-line and extract fields
    private void loadInputs(String pathToFile) {
        valleys.clear();
        try (BufferedReader br = new BufferedReader(new FileReader(pathToFile))) {
            String line;
            /** Replace this regex **/
//            Pattern p = Pattern.compile("([FB]{7})([LR]{3})");
            Valley currentValley = new Valley();
            Integer y = 0;
            while ((line = br.readLine()) != null) {
                // on a blank line, save the current valley and start a new one
                if (line.equals("")) {
                    valleys.add(currentValley);
                    currentValley = new Valley();
                    y = 0;
                } else {
                    // process the line.
                    for (int x = 0; x < line.length(); x++) {
                        XYPoint xy = new XYPoint(x, y);
                        currentValley.put(xy, line.charAt(x));
                    }
                    y++;
                }
            }
            // Add the last valley
            valleys.add(currentValley);

            // After we're done loading, calculate the row & column values
            for (Valley valley : valleys) {
                valley.calculateRowsAndCols();
            }
        } catch (Exception e) {
            System.out.println("Exception occurred: " + e.getMessage());
        }
    }
}
