package org.jgoeres.adventofcode2023.Day14;

import org.jgoeres.adventofcode.common.XYPoint;

import java.io.BufferedReader;
import java.io.FileReader;

import static org.jgoeres.adventofcode.common.Direction8Way.UP;
import static org.jgoeres.adventofcode2023.Day14.Platform.*;

public class Day14Service {
    public boolean DEBUG = false;

    private Platform platform = new Platform();

    public Day14Service(String pathToFile) {
        loadInputs(pathToFile);
    }

    public Day14Service(String pathToFile, boolean debug) {
        loadInputs(pathToFile);
        DEBUG = debug;
    }

    public long doPartA() {
        System.out.println("=== DAY 14A ===");

        Long totalLoad = 0L;
        /**
         * Tilt the platform so that the rounded rocks all roll north.
         * Afterward, what is the total load on the north support beams?
         **/

        // Roll everything north
        final XYPoint lookup = new XYPoint();
        final XYPoint north = new XYPoint();
        for (int y = 0; y <= platform.getYMax(); y++) {
            for (int x = 0; x <= platform.getXMax(); x++) {
                lookup.set(x, y);
                north.set(x, y - 1);
                if (platform.getMap().containsKey(lookup.toString())
                        && platform.getMap().get(lookup.toString()).equals(ROCK_ROUND)
                        && lookup.getY() > 0) {
                    // If this is a rock, try to roll it north
                    String original = lookup.toString();
                    Boolean moved = false;
                    while (lookup.getY() > 0
                            && !platform.getMap().containsKey(north.toString())) {
                        // As long as the thing above us isn't a rock of some kind, move there
                        lookup.moveRelative(1, UP);
                        north.moveRelative(1, UP);
                        moved = true;
                    }
                    // When we're done rolling, update the map
                    if (moved) {
                        platform.getMap().remove(original);
                        platform.put(lookup, ROCK_ROUND);
                    }
                }
            }
        }

        // Count up the score
        final XYPoint xy = new XYPoint();
        for (int y = 0; y <= platform.getYMax(); y++) {
            for (int x = 0; x <= platform.getXMax(); x++) {
                xy.set(x, y);
                if (platform.getMap().containsKey(xy.toString())
                        && platform.getMap().get(xy.toString()).equals(ROCK_ROUND)) {
                    // If this is a round rock, calculate its load
                    Long load = platform.getYMax() + 1 - xy.getY();
                    totalLoad += load;
                }
            }
        }

        System.out.println("Day 14A: Total load on north side = " + totalLoad);
        return totalLoad;
    }

    public long doPartB() {
        System.out.println("=== DAY 14B ===");

        long result = 0;
        /** Put problem implementation here **/

        System.out.println("Day 14B: Answer = " + result);
        return result;
    }

    // load inputs line-by-line and extract fields
    private void loadInputs(String pathToFile) {
        platform.getMap().clear();
        try (BufferedReader br = new BufferedReader(new FileReader(pathToFile))) {
            String line;
            /** Replace this regex **/
//            Pattern p = Pattern.compile("([FB]{7})([LR]{3})");
            Integer y = 0;
            while ((line = br.readLine()) != null) {
                // process the line.
                for (int x = 0; x < line.length(); x++) {
                    if (line.charAt(x) == ROCK_CUBE
                            || line.charAt(x) == ROCK_ROUND) {
                        XYPoint xy = new XYPoint(x, y);
                        platform.put(xy, line.charAt(x));
                    }
                }
                y++;
            }
            // When we're done, save the map, so we can revert later
            platform.save();
        } catch (
                Exception e) {
            System.out.println("Exception occurred: " + e.getMessage());
        }
    }
}
