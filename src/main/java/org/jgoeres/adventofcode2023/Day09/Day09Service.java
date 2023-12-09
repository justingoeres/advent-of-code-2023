package org.jgoeres.adventofcode2023.Day09;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Day09Service {
    public boolean DEBUG = false;

    private final List<List<Long>> sensorReadings = new ArrayList<>();

    public Day09Service(String pathToFile) {
        loadInputs(pathToFile);
    }

    public Day09Service(String pathToFile, boolean debug) {
        loadInputs(pathToFile);
        DEBUG = debug;
    }

    public long doPartA() {
        System.out.println("=== DAY 9A ===");
        /**
         * Analyze your OASIS report and extrapolate the next value for each history.
         * What is the sum of these extrapolated values?
         **/
        // Process each set of readings
        Long result = sensorReadings.stream()
                .map(this::calculateNextValue)
                .mapToLong(nextValue -> nextValue)
                .sum();
        System.out.println("Day 9A: Answer = " + result);
        return result;
    }

    public long doPartB() {
        System.out.println("=== DAY 9B ===");
        /**
         * Analyze your OASIS report again, this time extrapolating the previous value for each history.
         * What is the sum of these extrapolated values?
         **/
        Long result = sensorReadings.stream()
                .map(this::calculatePreviousValue)
                .mapToLong(nextValue -> nextValue)
                .sum();

        System.out.println("Day 9B: Answer = " + result);
        return result;
    }

    private Long calculateNextValue(final List<Long> readings) {
        // Make a note of the last known value in this set
        final Long lastValue = readings.get(readings.size() - 1);
        final List<Long> differences = new ArrayList<>();
        for (int i = 1; i < readings.size(); i++) {
            // find the difference between the ith reading and the (i-1)th
            differences.add(readings.get(i) - readings.get(i - 1));
        }
        // Do we have all zeroes yet?
        // If so, we're done!
        if (differences.stream().allMatch(d -> d == 0L)) {
            return lastValue;
        } else {
            Long nextValue = lastValue + calculateNextValue(differences);
            return nextValue;
        }
    }

    private Long calculatePreviousValue(final List<Long> readings) {
        // Make a note of the first value in this set
        final Long firstValue = readings.get(0);
        final List<Long> differences = new ArrayList<>();
        for (int i = 1; i < readings.size(); i++) {
            // find the difference between the ith reading and the (i-1)th
            differences.add(readings.get(i) - readings.get(i - 1));
        }
        // Do we have all zeroes yet?
        // If so, we're done!
        if (differences.stream().allMatch(d -> d == 0L)) {
            return firstValue;
        } else {
            Long prevValue = firstValue - calculatePreviousValue(differences);
            return prevValue;
        }
    }

    // load inputs line-by-line and extract fields
    private void loadInputs(String pathToFile) {
        sensorReadings.clear();
        try (BufferedReader br = new BufferedReader(new FileReader(pathToFile))) {
            String line;
            /** Replace this regex **/
            final Pattern p = Pattern.compile("-?\\d+");
//            10 13 27 64 140 281 549 ...
//            19 34 52 73 97 124 154 ...
//            17 26 44 90 198 421 827 ...
            while ((line = br.readLine()) != null) {
                // process the line.
                final Matcher m = p.matcher(line);
                final List<Long> readings = new ArrayList<>();
                while (m.find()) { // If our regex matched this line
                    // Parse it
                    final Long sensorReading = Long.parseLong(m.group(0));
                    readings.add(sensorReading);
                }
                sensorReadings.add(readings);
            }
        } catch (Exception e) {
            System.out.println("Exception occurred: " + e.getMessage());
        }
    }
}
