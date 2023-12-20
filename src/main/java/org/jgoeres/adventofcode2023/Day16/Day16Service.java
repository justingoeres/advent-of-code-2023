package org.jgoeres.adventofcode2023.Day16;

import org.jgoeres.adventofcode.common.DirectionURDL;
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
        traceBeam(xyBeam1, dirBeam1);
        long result = energized.size();
        System.out.println("Day 16A: Total tiles energized = " + result);
        return result;
    }

    public long doPartB() {
        System.out.println("=== DAY 16B ===");

        long result = 0L;
        /**
         * Find the initial beam configuration that energizes the largest number of tiles;
         * how many tiles are energized in that configuration?
         **/
        // Process all the configurations
        for (int x0 = 0; x0 <= xMax; x0++) { // down/up the columns
            // down from the top
            XYPoint xy0 = new XYPoint(x0, 0);
            DirectionURDL dir = DOWN;
            traceBeam(xy0, dir);    // run it
            result = Math.max(result, energized.size());

            // up from the bottom
            xy0.set(x0, yMax - 1);
            dir = UP;
            traceBeam(xy0, dir);    // run it
            result = Math.max(result, energized.size());
        }

        for (int y0 = 0; y0 < yMax; y0++) { // across the rows
            // in from the left
            XYPoint xy0 = new XYPoint(0, y0);
            DirectionURDL dir = RIGHT;
            traceBeam(xy0, dir);    // run it
            result = Math.max(result, energized.size());

            // in from the right
            xy0.set(xMax, y0);
            dir = LEFT;
            traceBeam(xy0, dir);    // run it
            result = Math.max(result, energized.size());
        }

        System.out.println("Day 16B: Max number of tiles energized = " + result);
        return result;
    }

    private void traceBeam(XYPoint xy0, DirectionURDL dir) {
//        System.out.printf("Testing %s going %s\n", xy0, dir);
        energized.clear();
        totalStates.clear();
        processBeamStep(xy0, dir);
    }

    private void processBeamStep(XYPoint xy, DirectionURDL dir) {
        // Go until we go out of bounds or hit a place we've been before
        while (inBounds(xy, xMax, yMax) && !totalStates.contains(getTotalState(xy, dir))) {
            energized.add(xy.toString());    // Energize the cell we're on
            totalStates.add(getTotalState(xy, dir));    // record the state in this tile

            // If we haven't already been in this exact state
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
                        break;
                    case (SPLIT_HORIZ):
                        if (dir.equals(UP) || dir.equals(DOWN)) {
                            // Split the beam
                            dir = RIGHT; // existing one goes right
                            xy.moveRelative(1, dir); // move the first one
                            // spawn the new one
                            final XYPoint newBeam = xy.getRelativeLocation(LEFT); // create a new beam to the left
                            final DirectionURDL newDir = LEFT; // the other keeps going left
                            // and process it
                            processBeamStep(newBeam, newDir);
                        } else {
                            // parallel to the splitter; just keep moving
                            xy.moveRelative(1, dir);
                        }
                        break;
                    case (SPLIT_VERT):
                        if (dir.equals(LEFT) || dir.equals(RIGHT)) {
                            // Split the beam
                            dir = UP; // existing one goes up
                            xy.moveRelative(1, dir); // move the first one
                            // spawn the new one
                            final XYPoint newBeam = xy.getRelativeLocation(DOWN); // create a new beam below
                            final DirectionURDL newDir = DOWN; // the other keeps going down
                            // and process it
                            processBeamStep(newBeam, newDir);
                        } else {
                            // parallel to the splitter; just keep moving
                            xy.moveRelative(1, dir);
                        }
                        break;
                }
            } else {
                // on an empty cell; just keep swimming
                xy.moveRelative(1, dir);
            }
        }
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
}
