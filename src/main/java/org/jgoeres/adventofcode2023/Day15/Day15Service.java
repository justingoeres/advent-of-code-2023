package org.jgoeres.adventofcode2023.Day15;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.*;

import static org.jgoeres.adventofcode2023.Day15.Step.REMOVE;

public class Day15Service {
    public boolean DEBUG = false;

    private List<Step> steps = new ArrayList<>();
    private List<String> initSequence = new ArrayList<>();
    private Map<Long, List<Step>> boxes = new HashMap<>();

    private static final String COMMA = ",";

    public Day15Service(String pathToFile) {
        loadInputs(pathToFile);
    }

    public Day15Service(String pathToFile, boolean debug) {
        loadInputs(pathToFile);
        DEBUG = debug;
    }

    public long doPartA() {
        System.out.println("=== DAY 15A ===");

        long totalValue = 0;
        /**
         * Determine the ASCII code for the current character of the string.
         * Increase the current value by the ASCII code you just determined.
         * Set the current value to itself multiplied by 17.
         * Set the current value to the remainder of dividing itself by 256.
         *
         * Run the HASH algorithm on each step in the initialization sequence. What is the sum of the results?
         **/

        for (String init : initSequence) {
            Long currentValue = calculateHash(init);
            totalValue += currentValue;
        }

        System.out.println("Day 15A: Sum of all HASHes = " + totalValue);
        return totalValue;
    }


    public long doPartB() {
        System.out.println("=== DAY 15B ===");

        long result = 0;
        /**
         * Arrange the steps in the boxes
         *
         * What is the focusing power of the resulting lens configuration?
         **/

        for (Step step : steps) {
            // Each step begins with a sequence of letters that indicate the label of the lens on which the step operates
            //
            // The label will be immediately followed by a character that indicates the
            // operation to perform: either an equals sign (=) or a dash (-).
            //
            // The result of running the HASH algorithm on the *label* indicates the correct box for that step.
            Long box = step.getHash();

            // If the operation character is a dash (-), go to the relevant box and remove the lens with the given label if it is present in the box.
            if (step.getOperation().equals(REMOVE)) {
                if (boxes.containsKey(box) && boxes.get(box).contains(step.getLabel()))
            }


        }

        System.out.println("Day 15B: Answer = " + result);
        return result;
    }

    public static Long calculateHash(String step) {
        Long currentValue = 0L;
        for (char c : step.toCharArray()) {
            currentValue += c;  // increase current value by ASCII code
            currentValue *= 17; // multiply by 17
            currentValue %= 256;    // remainder after dividing by 256
        }
        return currentValue;
    }

    // load inputs line-by-line and extract fields
    private void loadInputs(String pathToFile) {
        try (BufferedReader br = new BufferedReader(new FileReader(pathToFile))) {
            String line = br.readLine();
            for (String init : line.split(COMMA)) {
                initSequence.add(init);
                steps.add(new Step(init));
            }
            /** Replace this regex **/
//            Pattern p = Pattern.compile("([FB]{7})([LR]{3})");
//            while ((line = br.readLine()) != null) {
//                // process the line.
//                Matcher m = p.matcher(line);
//                if (m.find()) { // If our regex matched this line
//                    // Parse it
//                    String field1 = m.group(1);
//                    String field2 = m.group(2);
//                }
//            }
        } catch (Exception e) {
            System.out.println("Exception occurred: " + e.getMessage());
        }
    }
}
