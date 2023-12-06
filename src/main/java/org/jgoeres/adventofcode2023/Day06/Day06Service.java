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

    private final Set<BoatRace> boatRaces = new HashSet<>();
    private BoatRace oneLongRace;

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
        /** Calculate how many different ways there are to win for each race, then multiply those together **/
        for (BoatRace boatRace : boatRaces) {
            ButtonRange buttonRange = calculateHoldTimes(boatRace);
            Long waysToWin = buttonRange.getMax() - buttonRange.getMin() + 1;
            if (DEBUG)
                System.out.printf("time:\t%d\trecord distance:\t%d\tmin_hold:\t%d\tmax_hold:\t%d\tways to win:\t%d\n",
                        boatRace.getTime(), boatRace.getRecordDistance(), buttonRange.getMin(), buttonRange.getMax(), waysToWin);
            result *= waysToWin;
        }
        System.out.println("Day 6A: Product of all ways to win = " + result);
        return result;
    }

    public long doPartB() {
        System.out.println("=== DAY 6B ===");

        /** How many ways can you beat the record in the longer race? **/

        ButtonRange buttonRange = calculateHoldTimes(oneLongRace);
        Long waysToWin = buttonRange.getMax() - buttonRange.getMin() + 1;
        System.out.println("Day 6B: # of ways to win = " + waysToWin);
        return waysToWin;
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

            String oneLongRaceTime = "";
            String oneLongRaceDistance = "";
            while (m1.find() && m2.find()) {
                Long time = Long.parseLong(m1.group(0));
                Long distance = Long.parseLong(m2.group(0));
                boatRaces.add(new BoatRace(time, distance));

                oneLongRaceTime += m1.group(0);
                oneLongRaceDistance += m2.group(0);
            }
            oneLongRace = new BoatRace(Long.parseLong(oneLongRaceTime), Long.parseLong(oneLongRaceDistance));

        } catch (Exception e) {
            System.out.println("Exception occurred: " + e.getMessage());
        }
    }
}
