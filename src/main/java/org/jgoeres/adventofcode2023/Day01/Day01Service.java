package org.jgoeres.adventofcode2023.Day01;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class Day01Service {
    public boolean DEBUG = false;

    private ArrayList<Integer> inputList = new ArrayList<>();

    public Day01Service(String pathToFile) {
        loadInputs(pathToFile);
    }

    public Day01Service(String pathToFile, boolean debug) {
        loadInputs(pathToFile);
        DEBUG = debug;
    }

    public long doPartA() {
        System.out.println("=== DAY 1A ===");

        long result = inputList.stream().reduce(0, Integer::sum);
        /** Put problem implementation here **/

        System.out.println("Day 1A: Answer = " + result);
        return result;
    }

    public long doPartB() {
        System.out.println("=== DAY 1B ===");

        long result = 0;
        /** Put problem implementation here **/

        System.out.println("Day 1B: Answer = " + result);
        return result;
    }

    // load inputs line-by-line and apply a regex to extract fields
    private void loadInputs(String pathToFile) {
        /*
            cmpptgjc3qhcjxcbcqgqkxhrms
            9sixonefour
            eighttwo2twofour9
         */
        inputList.clear();
        try (BufferedReader br = new BufferedReader(new FileReader(pathToFile))) {
            String line;
            Integer nextInt = 0;
            /** Get the first digit, and last digit if it exists **/
            Pattern p = Pattern.compile("^[a-z]*(\\d)[a-z0-9]*?(\\d)?[a-z]*$");
            while ((line = br.readLine()) != null) {
                // process the line.
                Matcher m = p.matcher(line);
                if (m.find()) { // If our regex matched this line
                    // Parse it
                    // If there's no group 2, count group 1 as first AND last
                    Integer value = Integer.valueOf(m.group(1)) * 10
                            + (m.group(2) != null ? Integer.valueOf(m.group(2)) : Integer.valueOf(m.group(1)));

                    inputList.add(value);
                }
            }
        } catch (Exception e) {
            System.out.println("Exception occurred: " + e.getMessage());
        }
    }
}
