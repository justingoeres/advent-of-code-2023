package org.jgoeres.adventofcode2023.Day16;

import org.jgoeres.adventofcode.common.DirectionURDL;
import org.jgoeres.adventofcode.common.Utils.Pair;
import org.jgoeres.adventofcode.common.XYPoint;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.*;
import java.util.regex.Pattern;

import static org.jgoeres.adventofcode.common.DirectionURDL.*;

public class Day16Service {
    public boolean DEBUG = false;

    private Map<XYPoint, Character> mirrors = new HashMap<>();
    private Set<String> energized = new HashSet<>();
    private Set<String> totalStates = new HashSet<>();
    private static final char UL_LR = '\\';
    private static final char UR_LL = '/';
    private static final char SPLIT_HORIZ = '-';
    private static final char SPLIT_VERT = '|';
    private static final char EMPTY = '.';

    private Long xMax = 0L;
    private Long yMax = 0L;

    public Day16Service(String pathToFile) {
        loadInputs(pathToFile);
    }

    public Day16Service(String pathToFile, boolean debug) {
        loadInputs(pathToFile);
        DEBUG = debug;
    }

    public long doPartA() {
        System.out.println("=== DAY 16A ===");

        /**
         * With the beam starting in the top-left heading right, how many tiles end up being energized?
         **/
        XYPoint xyBeam1 = new XYPoint(0, 0); // Start in the upper left
        DirectionURDL dirBeam1 = RIGHT;    // headed to the Right

        processBeamStep(xyBeam1, dirBeam1);

        long result = energized.size();
//        printXYs(energized);
        System.out.println("Day 16A: Total tiles energized = " + result);
        return result;
    }

    private void processBeamStep(XYPoint xy, DirectionURDL dir) {
        if (inBounds(xy, xMax, yMax)) {
            // Energize the cell we're on
            energized.add(xy.toString());
            // If we haven't already been in this exact state
            if (!totalStates.contains(getTotalState(xy, dir))) {
                // Figure out what kind of tile we're on and execute it
                if (mirrors.containsKey(xy)) {
                    // If we're on a non-empty cell
                    final Character cellType = mirrors.get(xy);
                    switch (cellType.charValue()) {
                        case (UL_LR): // '\'
                            switch (dir) {
                                case UP:
                                    dir = LEFT;
                                    break;
                                case RIGHT:
                                    dir = DOWN;
                                    break;
                                case DOWN:
                                    dir = RIGHT;
                                    break;
                                case LEFT:
                                    dir = UP;
                                    break;
                            }
                            xy.moveRelative(1, dir); // move
                            processBeamStep(xy, dir);
                            break;
                        case (UR_LL): // '/'
                            switch (dir) {
                                case UP:
                                    dir = RIGHT;
                                    break;
                                case RIGHT:
                                    dir = UP;
                                    break;
                                case DOWN:
                                    dir = LEFT;
                                    break;
                                case LEFT:
                                    dir = DOWN;
                                    break;
                            }
                            xy.moveRelative(1, dir); // move
                            processBeamStep(xy, dir);
                            break;
                        case (SPLIT_HORIZ):
                            if (dir.equals(UP) || dir.equals(DOWN)) {
                                // Split the beam
//                                        System.out.printf("horizontal splitter going %s\n", dir);
                                dir = RIGHT; // existing one goes right
                                final XYPoint newBeam = xy.getRelativeLocation(LEFT); // create a new beam to the left
                                final DirectionURDL newDir = LEFT; // the other keeps going left
                                xy.moveRelative(1, dir); // move the first one
                                // store them both for next time
                                processBeamStep(xy, dir);
                                processBeamStep(newBeam, newDir);
                            } else {
                                // parallel to the splitter; just keep moving
                                xy.moveRelative(1, dir);
                                processBeamStep(xy, dir);
                            }
                            break;
                        case (SPLIT_VERT):
                            if (dir.equals(LEFT) || dir.equals(RIGHT)) {
                                // Split the beam
                                dir = UP; // existing one goes up
                                final XYPoint newBeam = xy.getRelativeLocation(DOWN); // create a new beam below
                                final DirectionURDL newDir = DOWN; // the other keeps going down
                                xy.moveRelative(1, dir); // move the first one
                                // store them both for next time
                                processBeamStep(xy, dir);
                                processBeamStep(newBeam, newDir);
                            } else {
                                // parallel to the splitter; just keep moving
                                xy.moveRelative(1, dir);
                                processBeamStep(xy, dir);
                            }
                            break;
                    }
                } else {
                    // on an empty cell
//                            System.out.printf("%s is EMPTY, adding to total states\tenergized:\t%d\n", xy, energized.size());
                    totalStates.add(getTotalState(xy, dir));    // record the state in this tile
                    // and then just keep moving
                    xy.moveRelative(1, dir);
                    processBeamStep(xy, dir);
                }
            } else {
                System.out.printf("beam dies at: row %d,\tcol %d\tgoing %s\n", xy.getY() + 1, xy.getX() + 1, dir);
            }
//                } else System.out.printf("beam dies at %s\n", xy);
        } else {
//                    System.out.printf("beam dies at: row %d,\tcol %d\t(out of bounds)\n", xy.getY() + 1, xy.getX() + 1);
        }
    }

    public long doPartAold() {
        System.out.println("=== DAY 16A ===");

        /**
         * With the beam starting in the top-left heading right, how many tiles end up being energized?
         **/
        XYPoint xyBeam1 = new XYPoint(0, 0); // Start in the upper left
        DirectionURDL dirBeam1 = RIGHT;    // headed to the Right

        final Map<XYPoint, DirectionURDL> beams1 = new HashMap<>();
        final Map<XYPoint, DirectionURDL> beams2 = new HashMap<>();
        beams1.put(xyBeam1, dirBeam1);    // setup the first beam

        Pair<Map<XYPoint, DirectionURDL>> beamPair = new Pair<>(beams1, beams2);

        Map<XYPoint, DirectionURDL> currentBeams = beamPair.getFirst();
        Integer t = 1;
        while (!currentBeams.isEmpty()) {
            System.out.printf("t = %d\n", t);
            final Map<XYPoint, DirectionURDL> nextBeams = beamPair.getSecond();
            nextBeams.clear();
//            print(mirrors, currentBeams);
            for (Map.Entry<XYPoint, DirectionURDL> beam : currentBeams.entrySet()) {
                // Process each beam at each step
                final XYPoint xy = beam.getKey();
                DirectionURDL dir = beam.getValue();
//                System.out.printf("beam at %s, heading %s\n", xy, dir);
                if (inBounds(xy, xMax, yMax)) {
                    // Energize the cell we're on
                    energized.add(xy.toString());
                    if (!totalStates.contains(getTotalState(xy, dir))) {
                        if (mirrors.containsKey(xy)) {
                            // If we're on a non-empty cell
                            final Character cellType = mirrors.get(xy);
                            switch (cellType.charValue()) {
                                case (UL_LR): // '\'
                                    switch (dir) {
                                        case UP:
                                            dir = LEFT;
                                            break;
                                        case RIGHT:
                                            dir = DOWN;
                                            break;
                                        case DOWN:
                                            dir = RIGHT;
                                            break;
                                        case LEFT:
                                            dir = UP;
                                            break;
                                    }
                                    xy.moveRelative(1, dir); // move
                                    nextBeams.put(xy, dir);
                                    break;
                                case (UR_LL): // '/'
                                    switch (dir) {
                                        case UP:
                                            dir = RIGHT;
                                            break;
                                        case RIGHT:
                                            dir = UP;
                                            break;
                                        case DOWN:
                                            dir = LEFT;
                                            break;
                                        case LEFT:
                                            dir = DOWN;
                                            break;
                                    }
                                    xy.moveRelative(1, dir); // move
                                    nextBeams.put(xy, dir);
                                    break;
                                case (SPLIT_HORIZ):
                                    if (dir.equals(UP) || dir.equals(DOWN)) {
                                        // Split the beam
//                                        System.out.printf("horizontal splitter going %s\n", dir);
                                        dir = RIGHT; // existing one goes right
                                        final XYPoint newBeam = xy.getRelativeLocation(LEFT); // create a new beam to the left
                                        final DirectionURDL newDir = LEFT; // the other keeps going left
                                        xy.moveRelative(1, dir); // move the first one
                                        // store them both for next time
                                        nextBeams.put(xy, dir);
                                        nextBeams.put(newBeam, newDir);
                                    } else {
                                        // parallel to the splitter; just keep moving
                                        xy.moveRelative(1, dir);
                                        nextBeams.put(xy, dir);
                                    }
                                    break;
                                case (SPLIT_VERT):
                                    if (dir.equals(LEFT) || dir.equals(RIGHT)) {
                                        // Split the beam
                                        dir = UP; // existing one goes up
                                        final XYPoint newBeam = xy.getRelativeLocation(DOWN); // create a new beam below
                                        final DirectionURDL newDir = DOWN; // the other keeps going down
                                        xy.moveRelative(1, dir); // move the first one
                                        // store them both for next time
                                        nextBeams.put(xy, dir);
                                        nextBeams.put(newBeam, newDir);
                                    } else {
                                        // parallel to the splitter; just keep moving
                                        xy.moveRelative(1, dir);
                                        nextBeams.put(xy, dir);
                                    }
                                    break;
                            }
                        } else {
                            // on an empty cell
//                            System.out.printf("%s is EMPTY, adding to total states\tenergized:\t%d\n", xy, energized.size());
                            totalStates.add(getTotalState(xy, dir));    // record the state in this tile
                            // and then just keep moving
                            xy.moveRelative(1, dir);
                            nextBeams.put(xy, dir);
                        }
                    } else {
                        System.out.printf("beam dies at: row %d,\tcol %d\tgoing %s\n", xy.getY() + 1, xy.getX() + 1, dir);
                    }
//                } else System.out.printf("beam dies at %s\n", xy);
                } else {
//                    System.out.printf("beam dies at: row %d,\tcol %d\t(out of bounds)\n", xy.getY() + 1, xy.getX() + 1);
                }
            }
            // Switch the beam sets and continue
            beamPair.swap();
            currentBeams = beamPair.getFirst();
//            System.out.printf("energized:\t%d\n", energized.size());
//            printAll(mirrors, currentBeams, energized);
//            System.out.printf("active beams:\t%d\n", currentBeams.size());
            t++;
        }
        long result = energized.size();
//        printXYs(energized);
        System.out.println("Day 16A: Total tiles energized = " + result);
        return result;
    }

    public long doPartB() {
        System.out.println("=== DAY 16B ===");

        long result = 0;
        /** Put problem implementation here **/

        System.out.println("Day 16B: Answer = " + result);
        return result;
    }

    private boolean inBounds(final XYPoint xy, final Long xMax, final Long yMax) {
        final Long x = xy.getX();
        final Long y = xy.getY();
        return (x >= 0 && x <= xMax
                && y >= 0 && y < yMax);
    }

    private String getTotalState(final XYPoint xy, final DirectionURDL dir) {
        return xy.toString() + ":" + dir;
    }

    private void print(final Map<XYPoint, Character> mirrors, final Map<XYPoint, DirectionURDL> currentBeams) {
        XYPoint xy = new XYPoint();
        for (int y = 0; y < yMax; y++) {
            String line = "";
            for (int x = 0; x <= xMax; x++) {
                xy.set(x, y);
                if (currentBeams.containsKey(xy)) {
                    final DirectionURDL dir = currentBeams.get(xy);
                    switch (dir) {
                        case UP:
                            line += "^";
                            break;
                        case RIGHT:
                            line += ">";
                            break;
                        case DOWN:
                            line += "v";
                            break;
                        case LEFT:
                            line += "<";
                            break;
                    }
                } else if (mirrors.containsKey(xy)) {
                    line += mirrors.get(xy);
                } else {
                    // empty
                    line += EMPTY;
                }
            }
            System.out.println(line);
        }
        System.out.println();
    }

    private void printAll(final Map<XYPoint, Character> mirrors, final Map<XYPoint, DirectionURDL> currentBeams, final Set<String> energized) {
        XYPoint xy = new XYPoint();
        for (int y = 0; y < yMax; y++) {
            String line = "";
            for (int x = 0; x <= xMax; x++) {
                xy.set(x, y);
                if (currentBeams.containsKey(xy)) {
                    final DirectionURDL dir = currentBeams.get(xy);
                    switch (dir) {
                        case UP:
                            line += "^";
                            break;
                        case RIGHT:
                            line += ">";
                            break;
                        case DOWN:
                            line += "v";
                            break;
                        case LEFT:
                            line += "<";
                            break;
                    }
                } else if (mirrors.containsKey(xy)) {
                    line += mirrors.get(xy);
                } else if (energized.contains(xy.toString())) {
                    line += "#";
                } else {
                    // empty
                    line += EMPTY;
                }
            }
            System.out.println(line);
        }
        System.out.println();
    }

    private void printEnergized(final Set<String> energized) {
        XYPoint xy = new XYPoint();
        for (int y = 0; y < yMax; y++) {
            String line = "";
            for (int x = 0; x <= xMax; x++) {
                xy.set(x, y);
                if (mirrors.containsKey(xy)) {
                    line += mirrors.get(xy);
//                    final DirectionURDL dir = currentBeams.get(xy);
//                    switch (dir) {
//                        case UP:
//                            line += "^";
//                            break;
//                        case RIGHT:
//                            line += ">";
//                            break;
//                        case DOWN:
//                            line += "v";
//                            break;
//                        case LEFT:
//                            line += "<";
//                            break;
//                    }
                } else if (energized.contains(xy.toString())) {
                    line += "#";
                } else {
                    // empty
                    line += EMPTY;
                }
            }
            System.out.println(line);
        }
        System.out.println();
    }

    // load inputs line-by-line and extract fields
    private void loadInputs(String pathToFile) {
        mirrors.clear();
        try (BufferedReader br = new BufferedReader(new FileReader(pathToFile))) {
            String line;
            /** Replace this regex **/
            Pattern p = Pattern.compile("([FB]{7})([LR]{3})");
            Long y = 0L;
            while ((line = br.readLine()) != null) {
                for (Integer x = 0; x < line.length(); x++) {
                    Character mirrorChar = line.charAt(x);
                    if (!mirrorChar.equals(EMPTY)) {
                        // If this is not an empty cell, put it in the map
                        XYPoint xy = new XYPoint(x, y);
                        mirrors.put(xy, mirrorChar);
                    }
                    if (x > xMax) xMax = Long.valueOf(x);
                }
                y++;
            }
            yMax = y;
        } catch (
                Exception e) {
            System.out.println("Exception occurred: " + e.getMessage());
        }
    }

    private void printXYs(Set<String> xySet) {
        xySet.stream().forEach(System.out::println);
    }
}
