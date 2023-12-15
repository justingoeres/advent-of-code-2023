package org.jgoeres.adventofcode2023.Day15;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class Day15Service {
    public boolean DEBUG = false;

    private List<String> initSequence;

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


        for (String step : initSequence) {
        System.out.println("Day 15A: Sum of all HASHes = " + totalValue);
            Long currentValue = 0L;
            for (char c : step.toCharArray()) {
                currentValue += c;  // increase current value by ASCII code
                currentValue *= 17; // multiply by 17
                currentValue %= 256;    // remainder after dividing by 256
            }
            totalValue += currentValue;

        }

        System.out.println("Day 15A: Answer = " + totalValue);
        return totalValue;
    }

    public long doPartB() {
        System.out.println("=== DAY 15B ===");

        long result = 0;
        /** Put problem implementation here **/

        System.out.println("Day 15B: Answer = " + result);
        return result;
    }

    // load inputs line-by-line and extract fields
    private void loadInputs(String pathToFile) {
        try (BufferedReader br = new BufferedReader(new FileReader(pathToFile))) {
            String line = br.readLine();
            initSequence = (List.of(line.split(COMMA)));
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
