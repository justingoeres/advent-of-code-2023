package org.jgoeres.adventofcode2023.Day11;

import org.jgoeres.adventofcode.common.AoCMath;
import org.jgoeres.adventofcode.common.XYPoint;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.LongStream;

public class Day11Service {
    public boolean DEBUG = false;

    final private ArrayList<XYPoint> galaxies = new ArrayList<>();
    private final Set<Long> blankRows = new HashSet<>();
    private final Set<Long> blankCols = new HashSet<>();

    public Day11Service(String pathToFile) {
        loadInputs(pathToFile);
    }

    public Day11Service(String pathToFile, boolean debug) {
        loadInputs(pathToFile);
        DEBUG = debug;
    }

    public long doPartA() {
        System.out.println("=== DAY 11A ===");

        /**
         * Expand the universe, then find the length of the shortest path between every pair of galaxies.
         * What is the sum of these lengths?
         **/
        final List<XYPoint> galaxiesPart1 = expandGalaxy(galaxies, 2L);
        final Long totalDistance = calculateDistances(galaxiesPart1);

        System.out.println("Day 11A: Sum of distances = " + totalDistance);
        return totalDistance;
    }

    public long doPartB(final Long factor) {
        System.out.println("=== DAY 11B ===");

        /**
         * Starting with the same initial image, expand the universe by 10000000 for each empty row/column,
         * then find the length of the shortest path between every pair of galaxies.
         * What is the sum of these lengths?
         **/

        final List<XYPoint> galaxiesPart2 = expandGalaxy(galaxies, factor);
        final Long totalDistance = calculateDistances(galaxiesPart2);

        System.out.println("Day 11B: Sum of distances = " + totalDistance);
        return totalDistance;
    }

    private Long calculateDistances(List<XYPoint> galaxiesPart2) {
        Long totalDistance = 0L;
        for (int i = 0; i < galaxiesPart2.size(); i++) {
            // measure its distance to all the other galaxies
            XYPoint galaxy1 = galaxiesPart2.get(i);
            for (int j = i + 1; j < galaxiesPart2.size(); j++) {
                // ... but only the ones we don't already know about
                XYPoint galaxy2 = galaxiesPart2.get(j);
                Long distance = AoCMath.manhattanDistance(galaxy1, galaxy2);
                if (DEBUG) {
                    System.out.printf("Between galaxy %d and galaxy %d: %d\n", i + 1, j + 1, distance);
                }
                totalDistance += distance;
            }
        }
        return totalDistance;
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
            blankRows.addAll(LongStream.range(0, yMax).boxed().collect(Collectors.toSet()));
            blankCols.addAll(LongStream.range(0, xMax).boxed().collect(Collectors.toSet()));
            for (XYPoint galaxy : galaxies) {
                blankRows.remove(galaxy.getY());
                blankCols.remove(galaxy.getX());
            }
        } catch (Exception e) {
            System.out.println("Exception occurred: " + e.getMessage());
        }
    }

    private List<XYPoint> expandGalaxy(final ArrayList<XYPoint> galaxies, final Long factor) {
        final List<XYPoint> expanded = new ArrayList<>();
        for (XYPoint galaxy : galaxies) {
            final Long xExpand = blankCols.stream().filter(col -> col < galaxy.getX()).count();
            final Long yExpand = blankRows.stream().filter(col -> col < galaxy.getY()).count();
            expanded.add(new XYPoint(galaxy.getX() + xExpand * (factor - 1), galaxy.getY() + yExpand * (factor - 1)));
        }
        return expanded;
    }
}
