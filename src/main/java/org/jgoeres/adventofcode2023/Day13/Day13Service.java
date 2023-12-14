package org.jgoeres.adventofcode2023.Day13;

import lombok.Getter;
import lombok.Setter;
import org.jgoeres.adventofcode.common.XYPoint;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;

public class Day13Service {
    public boolean DEBUG = false;
    public static final Integer NOT_FOUND_IDX = -1;

    private ArrayList<Valley> valleys = new ArrayList<>();

    public Day13Service(String pathToFile) {
        loadInputs(pathToFile);
    }

    public Day13Service(String pathToFile, boolean debug) {
        loadInputs(pathToFile);
        DEBUG = debug;
    }

    @Getter
    @Setter
    public static class Answers {
        Long partA;
        Long partB;
    }

    public Day13Service.Answers doPuzzle() {
        final Day13Service.Answers answers = new Day13Service.Answers();
        answers.setPartA(doPartA());
        answers.setPartB(doPartB());
        return answers;
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

        for (Valley valley : valleys) {
            Integer rowFoundIdx = searchValues(valley.getRowValues());
            valley.setRowReflectIdx(rowFoundIdx);
            Integer colFoundIdx = searchValues(valley.getColValues());
            valley.setColReflectIdx(colFoundIdx);
            Integer valleyResult = (colFoundIdx + 1) + 100 * (rowFoundIdx + 1); // because the puzzle counts from 1 not 0 }
            result += valleyResult;
        }

        System.out.println("Day 13A: Answer = " + result);
        return result;
    }

    public long doPartB() {
        System.out.println("=== DAY 13B ===");

        long result = 0;
        /**
         * In each pattern, fix the smudge and find the different line of reflection.
         * What number do you get after summarizing the new reflection
         * line in each pattern in your notes?
         **/

        for (Valley valley : valleys) {
            Integer rowFoundIdx = searchValuesPartB(valley.getRowValues(), valley.getRowReflectIdx());
            valley.setRowReflectIdx(rowFoundIdx);
            Integer colFoundIdx = searchValuesPartB(valley.getColValues(), valley.getColReflectIdx());
            valley.setColReflectIdx(colFoundIdx);
            Integer valleyResult = (colFoundIdx + 1) + 100 * (rowFoundIdx + 1); // because the puzzle counts from 1 not 0 }
            result += valleyResult;
        }

        System.out.println("Day 13B: Answer = " + result);
        return result;
    }

    private static Integer searchValues(List<Long> values) {
        int i = 0;
        while ((i + 1) < values.size()) {    // while we're in bounds
            // The first reflected column/row is the one that
            // is equal to its next neighbor
            if (values.get(i).equals(values.get(i + 1))) {
                int foundAtIdx = i;
                final Boolean isReflection = checkForMatches(foundAtIdx, values);
                if (isReflection) {
                    // If this is a reflection, return the result
                    return foundAtIdx;
                }
            }
            i++;
        }
        // If we get here, we didn't find any reflections
        return NOT_FOUND_IDX;
    }

    private static Integer searchValuesPartB(List<Long> values, Integer alreadyFoundAtIdx) {
        int i = 0;
        while ((i + 1) < values.size()) {    // while we're in bounds
            // The first reflected column/row is the one that
            // is equal to its next neighbor
            if (i != alreadyFoundAtIdx // if we already found a reflection here before, skip it
                    && (values.get(i).equals(values.get(i + 1))
                    || checkWithShifts(values.get(i), values.get(i + 1)))) {
                int foundAtIdx = i;
                final Boolean isReflection = checkForMatchesPartB(foundAtIdx, values);
                if (isReflection) {
                    // If this is a reflection, return the result
                    return foundAtIdx;
                }
            }
            i++;
        }
        // If we get here, we didn't find any reflections
        return NOT_FOUND_IDX;
    }

    private static Boolean checkWithShifts(final Long val1, final Long val2) {
        for (int b = 0; b < 24; b++) {  // All our numbers are < 24 bits
            Long val2Fixed = val2 ^ (1 << b);
            if (val1.equals(val2Fixed)) {
                // If the "fixed" side 2 value now matches side 1
                // this could still be a reflection!
                return true;
            }
        }
        // If we get here, nothing matched, so
        return false;
    }

    private static Boolean checkForMatchesPartB(int foundAt, List<Long> values) {
        // Now that we found a candidate, check backwards to verify the match
        int side1 = foundAt;
        int side2 = foundAt + 1;
        Boolean found = false;   // assume a match until we find out different
        while (side1 >= 0 && side2 < values.size()) {
            // as long as we're in bounds and haven't found something yet
            if (!values.get(side1).equals(values.get(side2))) {
                // If the two values are NOT equal
                // Can we fix one of the values?
                Boolean keepGoing = false;
                for (int b = 0; b < 24; b++) {  // All our numbers are < 24 bits
                    Long valueSide2Fixed = values.get(side2) ^ (1 << b);
                    if (values.get(side1).equals(valueSide2Fixed)) {
                        // If the "fixed" side 2 value now matches side 1
                        // this could still be a reflection!
                        keepGoing = true;
                        break;
                    }
                }
                if (!keepGoing) return false;
            }
            side1--;
            side2++;
        }
        // If we get here, we're done checking and haven't failed, so this is a reflection.
        return true;
    }

    private static Boolean checkForMatches(int foundAt, List<Long> values) {
        // Now that we found a candidate, check backwards to verify the match
        int side1 = foundAt;
        int side2 = foundAt + 1;
        while (side1 >= 0 && side2 < values.size()) {
            // as long as we're in bounds
            if (!values.get(side1).equals(values.get(side2))) {
                // If the two values are NOT equal
                // Check to see if there's a smudge we can fix
                return false;
            }
            side1--;
            side2++;
        }
        // If we get here, we're done checking and haven't failed, so this is a reflection.
        return true;
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
