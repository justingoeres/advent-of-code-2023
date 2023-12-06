package org.jgoeres.adventofcode2023.Day06;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Day06Service {
    public boolean DEBUG = false;

    private Set<BoatRace> boatRaces = new HashSet<>();

    public Day06Service(String pathToFile) {
        loadInputs(pathToFile);
    }

    public Day06Service(String pathToFile, boolean debug) {
        loadInputs(pathToFile);
        DEBUG = debug;
    }

    public long doPartA() {
        System.out.println("=== DAY 6A ===");

        long result = 1;
        /** Put problem implementation here **/
        for (BoatRace boatRace : boatRaces) {
            ButtonRange buttonRange = calculateHoldTimes(boatRace);
            Long waysToWin = buttonRange.getMax() - buttonRange.getMin() + 1;
            System.out.printf("time:\t%d\trecord distance:\t%d\tmin_hold:\t%d\tmax_hold:\t%d\tways to win:\t%d\n",
                    boatRace.getTime(), boatRace.getRecordDistance(), buttonRange.getMin(), buttonRange.getMax(), waysToWin);
            result *= waysToWin;
        }
        System.out.println("Day 6A: Answer = " + result);
        return result;
    }

    public long doPartB() {
        System.out.println("=== DAY 6B ===");

        long result = 0;
        /** Put problem implementation here **/

        System.out.println("Day 6B: Answer = " + result);
        return result;
    }

    private ButtonRange calculateHoldTimes(BoatRace boatRace) {
        // The button hold times that will beat the record distance
        // are all the t_hold times between (inclusive) the roots of the quadratic equation
        //      t_hold^2 - t_race * t_hold + d_race = 0
        final Long min = (long) Math.ceil((boatRace.getTime() - Math.sqrt(Math.pow(boatRace.getTime(), 2) - 4 * boatRace.getRecordDistance())) / 2 + .00000001); // the .00000001 is to handle cases where the answer is exact; we need to be strictly > and d, those
        final Long max = (long) Math.floor((boatRace.getTime() + Math.sqrt(Math.pow(boatRace.getTime(), 2) - 4 * boatRace.getRecordDistance())) / 2 - .00000001);

        return new ButtonRange(min, max);
    }

    // load inputs line-by-line and extract fields
    private void loadInputs(String pathToFile) {
        boatRaces.clear();
        try (BufferedReader br = new BufferedReader(new FileReader(pathToFile))) {
            String line;
            /** Replace this regex **/
            Pattern p = Pattern.compile("\\d+");
            // Just read the two lines
            String times = br.readLine();
            String distances = br.readLine();

            Matcher m1 = p.matcher(times);
            Matcher m2 = p.matcher(distances);

            while (m1.find() && m2.find()) {
                Long time = Long.parseLong(m1.group(0));
                Long distance = Long.parseLong(m2.group(0));
                boatRaces.add(new BoatRace(time, distance));
            }
        } catch (Exception e) {
            System.out.println("Exception occurred: " + e.getMessage());
        }
    }
}
