package org.jgoeres.adventofcode2023.Day03;

import org.jgoeres.adventofcode.common.Direction8Way;
import org.jgoeres.adventofcode.common.DirectionURDL;
import org.jgoeres.adventofcode.common.XYPoint;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Day03Service {
    public boolean DEBUG = false;

    private final ArrayList<Integer> inputList = new ArrayList<>();
    private final Map<XYPoint, String> partNumbers = new HashMap<>();
    private final Map<XYPoint, String> symbols = new HashMap<>();

    public Day03Service(String pathToFile) {
        loadInputs(pathToFile);
    }

    public Day03Service(String pathToFile, boolean debug) {
        loadInputs(pathToFile);
        DEBUG = debug;
    }

    public long doPartA() {
        System.out.println("=== DAY 3A ===");

        /**
         * Any number adjacent to a symbol, even diagonally, is a "part number" and should be included in your sum
         *
         * What is the sum of all of the part numbers in the engine schematic?
         * **/
        long result = partNumbers.entrySet().stream()
                .filter(partNumber -> checkForSymbol(partNumber.getKey(), partNumber.getValue(), symbols))
                .map(partNumber -> Integer.parseInt(partNumber.getValue()))
                .reduce(0, Integer::sum);

        // There are fewer symbols than
        System.out.println("Day 3A: Answer = " + result);
        return result;
    }

    public long doPartB() {
        System.out.println("=== DAY 3B ===");

        long result = 0;
        /** Put problem implementation here **/

        System.out.println("Day 3B: Answer = " + result);
        return result;
    }

    // load inputs line-by-line and apply a regex to extract fields
    private void loadInputs(String pathToFile) {
        inputList.clear();
        partNumbers.clear();
        symbols.clear();
        try (BufferedReader br = new BufferedReader(new FileReader(pathToFile))) {
            String line;
            Integer nextInt = 0;
            Integer y = 0;  // line number
            /** Match part numbers & symbols **/
            final Pattern p1 = Pattern.compile("\\d+");
            final Pattern p2 = Pattern.compile("[^0-9.]");
            while ((line = br.readLine()) != null) {
                // process the line.
                final Matcher m1 = p1.matcher(line);
                // Find the part numbers
                while (m1.find()) {
                    final String partNumber = m1.group(); // part number
                    final Integer x = m1.start(); // column number
                    partNumbers.put(new XYPoint(x, y), partNumber);
                }
                // Find the symbols
                final Matcher m2 = p2.matcher(line);
                while (m2.find()) { // find all the numbers
                    final String symbol = m2.group(); // part number
                    final Integer x = m2.start(); // column number
                    symbols.put(new XYPoint(x, y), symbol);
                }
                y += 1; // next line
            }
        } catch (Exception e) {
            System.out.println("Exception occurred: " + e.getMessage());
        }
    }

    private boolean checkForSymbol(final XYPoint xy0, final String partNumber, final Map<XYPoint, String> symbols) {
        // Check all the points around the partNumber; if any of them contain a symbol, return true

        // Build the set of steps to take
        ArrayList<DirectionURDL> steps = new ArrayList<>();
        steps.add(DirectionURDL.LEFT);  // start to the left of the part number
        steps.add(DirectionURDL.UP);    // then go up
        for (int i = 0; i < partNumber.length() + 1; i++)   // then go right, past the end of the number
            steps.add(DirectionURDL.RIGHT);
        steps.add(DirectionURDL.DOWN);  // then down twice
        steps.add(DirectionURDL.DOWN);
        for (int i = 0; i < partNumber.length() + 1; i++)   // then go left, past the beginning of the number
            steps.add(DirectionURDL.LEFT);

        // Now check all those points
        XYPoint xy = new XYPoint(xy0.getX(), xy0.getY());
        if (DEBUG) System.out.println("Checking around " + partNumber + " at " + xy);
        for (DirectionURDL step : steps) {
            xy.moveRelative(1, step);
            if (symbols.containsKey(xy)) {
                if (DEBUG) System.out.println("Found symbol " + symbols.get(xy) + " at " + xy);
                return true;
            }   // if we found a symbol
        }
        // otherwise return false
        return false;

    }
}
