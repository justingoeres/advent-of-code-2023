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
    private static final List<Integer> nONES = List.of(1, 2, 3, 4, 5, 6, 7, 8, 9);
    private static final List<String> wONES = List.of("one", "two", "three", "four", "five", "six",
            "seven", "eight", "nine");

    private ArrayList<Integer> inputList1 = new ArrayList<>();
    private ArrayList<Integer> inputList2 = new ArrayList<>();

    public Day01Service(String pathToFile) {
        loadInputs(pathToFile);
    }

    public Day01Service(String pathToFile, boolean debug) {
        loadInputs(pathToFile);
        DEBUG = debug;
    }

    public long doPartA() {
        System.out.println("=== DAY 1A ===");

        /** What is the sum of all the calibration values? **/
        long result = inputList1.stream().reduce(0, Integer::sum);

        System.out.println("Day 1A: Sum of calibration values = " + result);
        return result;
    }

    public long doPartB() {
        System.out.println("=== DAY 1B ===");

        /** What is the sum of all the calibration values? **/
        long result = inputList2.stream().reduce(0, Integer::sum);

        System.out.println("Day 1B: Sum of calibration values = " + result);
        return result;
    }

    // load inputs line-by-line and extract fields
    private void loadInputs(String pathToFile) {
        /*
            cmpptgjc3qhcjxcbcqgqkxhrms
            9sixonefour
            eighttwo2twofour9
         */
        inputList1.clear();
        inputList2.clear();
        try (BufferedReader br = new BufferedReader(new FileReader(pathToFile))) {
            String line;
            /** Get the first digit, and last digit if it exists **/
            Pattern p1 = Pattern.compile("^[a-z]*(\\d)[a-z0-9]*?(\\d)?[a-z]*$");
            Pattern p2fwd = Pattern.compile("(\\d|one|two|three|four|five|six|seven|eight|nine)");
            Pattern p2back = Pattern.compile("(\\d|enin|thgie|neves|xis|evif|ruof|eerht|owt|eno)");
            while ((line = br.readLine()) != null) {
                // Part 1 Parsing:
                Matcher m1 = p1.matcher(line);
                if (m1.find()) { // If our regex matched this line
                    // Parse it
                    // If there's no group 2, count group 1 as first AND last
                    Integer value = Integer.valueOf(m1.group(1)) * 10
                            + (m1.group(2) != null ? Integer.valueOf(m1.group(2)) : Integer.valueOf(m1.group(1)));
                    inputList1.add(value);
                }

                // Part 2 Parsing:
                Matcher m2fwd = p2fwd.matcher(line);
                Matcher m2back = p2back.matcher(new StringBuffer(line).reverse().toString());
                m2fwd.find();  // search from the front for the first digit
                String part2digit1 = m2fwd.group(1);
                m2back.find();  // search from the back for the second digit
                String part2digit2 = m2back.group(1);
                inputList2.add(digitValue(part2digit1) * 10 + digitValue(new StringBuffer(part2digit2).reverse().toString()));
            }
        } catch (Exception e) {
            System.out.println("Exception occurred: " + e.getMessage());
        }
    }

    private final Integer digitValue(String digitString) {
        if (digitString.chars().allMatch(Character::isDigit)) {
            // if it's a digit
            return Integer.valueOf(digitString);
        } else {
            // if it's a word
            return nONES.get(wONES.indexOf(digitString));
        }
    }
}
