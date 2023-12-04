package org.jgoeres.adventofcode2023.Day03;

import org.jgoeres.adventofcode.common.DirectionURDL;
import org.jgoeres.adventofcode.common.XYPoint;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Day03Service {
    public boolean DEBUG = false;
    private static final String GEAR = "*";

    // A gear is just one character (*) so there are always the same 8 steps around it
    private static final List<DirectionURDL> gearSteps = List.of(DirectionURDL.LEFT,
            DirectionURDL.UP,
            DirectionURDL.RIGHT,
            DirectionURDL.RIGHT,
            DirectionURDL.DOWN,
            DirectionURDL.DOWN,
            DirectionURDL.LEFT,
            DirectionURDL.LEFT
    );

    private final ArrayList<Integer> inputList = new ArrayList<>();
    private final Map<XYPoint, String> partNumbers = new HashMap<>();
    private final Map<XYPoint, String> partNumberCoverage = new HashMap<>();
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
         * What is the sum of all the part numbers in the engine schematic?
         **/
        long result = partNumbers.entrySet().stream()
                .filter(partNumber -> checkForSymbol(partNumber.getKey(), partNumber.getValue(), symbols))
                .map(partNumber -> Integer.parseInt(partNumber.getValue()))
                .reduce(0, Integer::sum);
        System.out.println("Day 3A: Sum of all part numbers = " + result);
        return result;
    }

    public long doPartB() {
        System.out.println("=== DAY 3B ===");
        /**
         * A gear is any * symbol that is adjacent to exactly two part numbers.
         * Its gear ratio is the result of multiplying those two numbers together.
         *
         * What is the sum of all the gear ratios in your engine schematic?
         **/
        // Filter out all the non-* symbols
        long result = symbols.entrySet().stream()
                .filter(g -> g.getValue().equals(GEAR))
                .map(g -> findGearRatio(g.getKey(), partNumberCoverage))
                .reduce(0, Integer::sum);
        System.out.println("Day 3B: Sum of all gear ratios = " + result);
        return result;
    }

    // load inputs line-by-line and apply a regex to extract fields
    private void loadInputs(String pathToFile) {
        inputList.clear();
        partNumbers.clear();
        symbols.clear();
        try (BufferedReader br = new BufferedReader(new FileReader(pathToFile))) {
            String line;
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
                    // Add this part number to the map
                    partNumbers.put(new XYPoint(x, y), partNumber);

                    // Also calculate its total coverage (for Part B)
                    for (int i = 0; i < partNumber.length(); i++) {
                        partNumberCoverage.put(new XYPoint(x + i, y), partNumber);
                    }
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
                // if we found a symbol
                if (DEBUG) System.out.println("Found symbol " + symbols.get(xy) + " at " + xy);
                return true;
            }
        }
        // otherwise return false
        return false;
    }

    private Integer findGearRatio(final XYPoint xy0, final Map<XYPoint, String> partNumberCoverage) {
        /**
         * A gear is any * symbol that is adjacent to exactly two part numbers.
         * Its gear ratio is the result of multiplying those two numbers together.
         **/
        // Figure out if this gear is next to EXACTLY two parts
        Set<String> parts = new HashSet<>();
        XYPoint xy = new XYPoint(xy0.getX(), xy0.getY());

        if (DEBUG) System.out.println("Checking around gear at " + xy);
        // Build the list of steps to take around the gear
        for (DirectionURDL step : gearSteps) {
            xy.moveRelative(1, step);
            if (partNumberCoverage.containsKey(xy)) {
                // if a part number covers this XY point, add it to the set of parts for this gear
                // NOTE: Using a Set prevents duplicates, but assumes a gear is never surrounded by two identical parts.
                parts.add(partNumberCoverage.get(xy));
            }
            // If a gear is bordered by more than two parts, it doesn't count; return 0.
            if (parts.size() > 2) {
                return 0;
            }
        }
        if (parts.size() == 2) {
            // It the gear is bordered by exactly two parts, return the product
            return parts.stream().map(p -> Integer.parseInt(p))
                    .reduce(1, Math::multiplyExact);
        } else {
            // Only bordered by one part; doesn't count. Return 0.
            return 0;
        }
    }
}
