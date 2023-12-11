package org.jgoeres.adventofcode2023.Day11;

import org.jgoeres.adventofcode.common.AoCMath;
import org.jgoeres.adventofcode.common.XYPoint;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class Day11Service {
    public boolean DEBUG = false;

    private ArrayList<XYPoint> galaxies = new ArrayList<>();

    public Day11Service(String pathToFile) {
        loadInputs(pathToFile);
    }

    public Day11Service(String pathToFile, boolean debug) {
        loadInputs(pathToFile);
        DEBUG = debug;
    }

    public long doPartA() {
        System.out.println("=== DAY 11A ===");

        /** Put problem implementation here **/
        final Map<XYPoint, XYPoint> measured = new HashMap<>();
        Long totalDistance = 0L;
        // For each galaxy
        for (int i = 0; i < galaxies.size(); i++) {
            // measure its distance to all the other galaxies
            XYPoint galaxy1 = galaxies.get(i);
            for (int j = i + 1; j < galaxies.size(); j++) {
                // ... but only the ones we don't already know about
                XYPoint galaxy2 = galaxies.get(j);
                Integer distance = AoCMath.manhattanDistance(galaxy1, galaxy2);
                if (DEBUG) {
                    System.out.printf("Between galaxy %d and galaxy %d: %d\n", i + 1, j + 1, distance);
                }
                totalDistance += distance;
            }
        }

        System.out.println("Day 11A: Answer = " + totalDistance);
        return totalDistance;
    }

    public long doPartB() {
        System.out.println("=== DAY 11B ===");

        long result = 0;
        /**
         * Starting with the same initial image, expand the universe by 10000000 for each empty row/column,
         * then find the length of the shortest path between every pair of galaxies.
         * What is the sum of these lengths? **/

        System.out.println("Day 11B: Answer = " + result);
        return result;
    }

    // load inputs line-by-line and extract fields
    private void loadInputs(String pathToFile) {
        galaxies.clear();
        try (BufferedReader br = new BufferedReader(new FileReader(pathToFile))) {
            String line;
            Integer y = 0;
            Integer xMax = 0;
            Integer yMax;
            /** Replace this regex **/
            Pattern p = Pattern.compile("#");
            while ((line = br.readLine()) != null) {
                xMax = line.length();
                // process the line.
                Matcher m = p.matcher(line);
                while (m.find()) { // Find all the galaxies
                    // For each one, record an XYPoint with its x & y coords
                    Integer x = m.start(0);
                    galaxies.add(new XYPoint(x, y));
                }
                y++;
            }
            yMax = y;

            // Now find the blank columns & rows
            Set<Integer> blankRows = IntStream.range(0, yMax).boxed().collect(Collectors.toSet());
            Set<Integer> blankCols = IntStream.range(0, xMax).boxed().collect(Collectors.toSet());
            for (XYPoint galaxy : galaxies) {
                blankRows.remove(galaxy.getY());
                blankCols.remove(galaxy.getX());
            }

            // Now that we have the blank rows & columns, expand the universe
            for (XYPoint galaxy : galaxies) {
                Long xExpand = blankCols.stream().filter(col -> col < galaxy.getX()).count();
                Long yExpand = blankRows.stream().filter(col -> col < galaxy.getY()).count();
                galaxy.setX((int) (galaxy.getX() + xExpand));
                galaxy.setY((int) (galaxy.getY() + yExpand));
            }
            System.out.println("ok");
        } catch (Exception e) {
            System.out.println("Exception occurred: " + e.getMessage());
        }
    }
}
