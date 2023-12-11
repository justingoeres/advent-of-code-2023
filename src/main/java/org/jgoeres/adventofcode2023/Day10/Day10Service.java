package org.jgoeres.adventofcode2023.Day10;

import org.jgoeres.adventofcode.common.Direction8Way;
import org.jgoeres.adventofcode.common.Utils.Pair;
import org.jgoeres.adventofcode.common.XYPoint;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.*;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import static org.jgoeres.adventofcode.common.Direction8Way.*;
import static org.jgoeres.adventofcode2023.Day10.PipeType.*;

public class Day10Service {
    public boolean DEBUG = false;

    private Map<String, Pipe> pipes = new HashMap();
    private Map<String, Pipe> theLoop;
    private Set<XYPoint> empties = new HashSet<>();
    private Pipe start;
    public static final char EMPTY = '.';
    private Integer xMax;
    private Integer yMax;


    public Day10Service(String pathToFile) {
        loadInputs(pathToFile);
    }

    public Day10Service(String pathToFile, boolean debug) {
        loadInputs(pathToFile);
        DEBUG = debug;
    }

    public long doPartA() {
        System.out.println("=== DAY 10A ===");

        /**
         * Find the single giant loop starting at S.
         * How many steps along the loop does it take to get
         * from the starting position to the point farthest
         * from the starting position?
         **/

        // Start at the START pipe and go both directions.
        // See how far we can get before we run into each other
        long stepCount = 0;
        Set<Pipe> currentPipes = new HashSet();
        Set<Pipe> nextPipes = new HashSet();
        currentPipes.add(start);
        final HashSet<Pipe> visited = new HashSet();

        final Pair<Set<Pipe>> currentNextPair = new Pair(currentPipes, nextPipes);
        Boolean found = false;
        while (!found) {
            for (Pipe current : currentPipes) {
                // for each current pipe, find the connections we haven't already visited
                nextPipes.addAll(current.getConnections().values()
                        .stream().filter(c -> !visited.contains(c))
                        .collect(Collectors.toSet()));
                if (visited.containsAll(nextPipes)) {
                    // If we find a solution, flag it and don't iterate anymore
                    visited.add(current);
                    found = true;
                    break;
                }
            }
            if (!found) {
                stepCount++;    // increment the count
                visited.addAll(currentPipes);   // mark these pipes visited
                // Swap current for next
                currentNextPair.swap();
                currentPipes = currentNextPair.getFirst();
                nextPipes = currentNextPair.getSecond();
                nextPipes.clear();
            }
        }
        // Populate "the loop" for Part B with all the pipes we just visited

        theLoop = visited.stream().collect(Collectors.toMap(pipe -> pipe.getXy().toString(), pipe -> pipe));
        System.out.println("Day 10A: Furthest distance from Start = " + stepCount);
        return stepCount;
    }


    public long doPartB() {
        System.out.println("=== DAY 10B ===");

        long result = 0;
        /**
         * Figure out whether you have time to search for the nest by calculating the area within the loop.
         * How many tiles are enclosed by the loop?
         **/

        // Note we only care about *empty* locations, nothing with a pipe in it
        // We also only care about things enclosed by *the loop*. So we can take the
        // empties and forget all the non-loop-connected pipes.
        //
        // The trick is just handling the 'squeezing through' bits

        // Clone the set of empties. There's probably a better way to do this.
        Set<XYPoint> insides = empties.stream().collect(Collectors.toSet());
        Set<XYPoint> explorers = new HashSet();
        Set<XYPoint> nextExplorers = new HashSet();
        Pair<Set<XYPoint>> explorersPair = new Pair(explorers, nextExplorers);
        Set<XYPoint> allVisited = new HashSet<>();
        Set<XYPoint> explorersVisited = new HashSet<>();
        explorersVisited.clear();

        for (XYPoint nextEmpty : empties) {
            // If we've already been here, just skip to the next one
            if (allVisited.contains(nextEmpty)) continue;

            // For each empty, fill outward until we hit either an edge or a part of theLoop
            // When we're out of places to go, check and see if we hit an edge. If we did,
            // then all the points we visited are OUTSIDE the loop and can be removed from 'insides'
            explorers.clear();
            explorers.add(nextEmpty); // start from the current empty
            explorersVisited.clear();
            explorersVisited.add(nextEmpty);
            Boolean done = false;

            System.out.printf("Checking from %s\n", nextEmpty);
            while (!done) {
                Set<XYPoint> neighbors = new HashSet<>();
                for (XYPoint explorer : explorers) {
                    // Get all the surrounding points of this xy
                    explorersVisited.add(explorer);
                    PipeType explorerType = theLoop.containsKey(explorer.toString()) ? theLoop.get(explorer.toString()).getType() : PipeType.EMPTY;
                    switch (explorerType) {
                        // populate nextExplorers based on valid neighbors given what kind of pipe we're on (or empty)
                        case START:
                            // don't go anywhere from the start, let other explorers take care of it
                            break;
                        case EMPTY:
                            neighbors.add(explorer.getRelativeLocation8Way(UP));
                            neighbors.add(explorer.getRelativeLocation8Way(RIGHT));
                            neighbors.add(explorer.getRelativeLocation8Way(DOWN));
                            neighbors.add(explorer.getRelativeLocation8Way(LEFT));
                            break;
                        case VERT:
                            // Can't go across a vertical pipe
                            neighbors.add(explorer.getRelativeLocation8Way(UP));
                            neighbors.add(explorer.getRelativeLocation8Way(DOWN));
                            break;
                        case HORIZ:
                            // Can't go across a horizontal pipe
                            neighbors.add(explorer.getRelativeLocation8Way(RIGHT));
                            neighbors.add(explorer.getRelativeLocation8Way(LEFT));
                            break;
                        case NORTH_EAST:    // L
                            // How to handle this? We can't cross it but doesn't direction matter?
                            // Can go up or right, but not down or left?
                            neighbors.add(explorer.getRelativeLocation8Way(UP));
                            neighbors.add(explorer.getRelativeLocation8Way(RIGHT));
                            break;
                        case NORTH_WEST:    // J
                            // Can go up or left
                            neighbors.add(explorer.getRelativeLocation8Way(UP));
                            neighbors.add(explorer.getRelativeLocation8Way(LEFT));
                            break;
                        case SOUTH_WEST:    // 7
                            // Can go down or left
                            neighbors.add(explorer.getRelativeLocation8Way(DOWN));
                            neighbors.add(explorer.getRelativeLocation8Way(LEFT));
                            break;
                        case SOUTH_EAST:
                            // Can go down or right
                            neighbors.add(explorer.getRelativeLocation8Way(RIGHT));
                            neighbors.add(explorer.getRelativeLocation8Way(DOWN));
                            break;
                    }
                }
                // Record any places we've visited
                allVisited.addAll(explorersVisited);
                // Remove any neighbors we've already visited
                nextExplorers = neighbors.stream()
                        .filter(xy -> (!explorersVisited.contains(xy)
                                && xy.getX() >= 0 && xy.getX() < xMax
                                && xy.getY() >= 0 && xy.getY() < yMax))
                        .collect(Collectors.toSet());
//                explorersPair.swap();
//                explorers = explorersPair.getFirst();
//                nextExplorers = explorersPair.getSecond();
                explorers = nextExplorers;

                // Check to see if we're done.
                // Go until we've been everywhere we can reach?
                done = explorers.isEmpty();
            }
            // Did any of our explorers reach an edge?
            Integer oldInsidesSize = insides.size();
            if (explorersVisited.stream().anyMatch(xyPoint -> xyPoint.getX() == 0 || xyPoint.getX() == (xMax - 1)
                    || xyPoint.getY() == 0 || xyPoint.getY() == (yMax - 1))) {
                // We reached an edge, so remove all of those from 'insides' because they're not 'inside'!
                insides.removeAll(explorersVisited);
                System.out.printf("%d points visited; insides was %d, is now %d\n", explorersVisited.size(), oldInsidesSize, insides.size());
            }
            System.out.println("Going to next empty");
        }
        result = insides.size();
        System.out.println("Day 10B: Answer = " + result);
        return result;
    }

    // load inputs line-by-line and extract fields
    private void loadInputs(String pathToFile) {
        pipes.clear();
        try (BufferedReader br = new BufferedReader(new FileReader(pathToFile))) {
            String line;
            /** Replace this regex **/
            Pattern p = Pattern.compile("([FB]{7})([LR]{3})");
            Integer y = 0;
            while ((line = br.readLine()) != null) {
                xMax = line.length();
                // process the line.
                for (int x = 0; x < line.length(); x++) {
                    char pipeChar = line.charAt(x);
                    XYPoint xy = new XYPoint(x, y);
                    if (pipeChar != EMPTY) {
                        // First, just create the pipes but don't attach them
                        PipeType type = PipeType.fromChar(pipeChar);
                        Pipe newPipe = new Pipe(type, xy);
                        pipes.put(xy.toString(), newPipe);
                        if (type == START) start = newPipe;
                    } else {
                        // It's empty, so add it to that Set
                        empties.add(xy);
                    }
                }
                y++;
            }
            yMax = y;
            // Now that we've got all the pipes, connect them up to each other
            for (Pipe pipe : pipes.values()) {
                XYPoint xy = pipe.getXy();
//                System.out.printf("%d\t%s\n", i, xy);
                switch (pipe.getType()) {
                    case START:
                        // do nothing, the other connections will take care of the start
                        break;
                    case VERT:
                        pipe.addConnection(UP, pipes.get(xy.getRelativeLocation(UP).toString()));
                        pipe.addConnection(DOWN, pipes.get(xy.getRelativeLocation(DOWN).toString()));
                        break;
                    case HORIZ:
                        pipe.addConnection(LEFT, pipes.get(xy.getRelativeLocation(LEFT).toString()));
                        pipe.addConnection(RIGHT, pipes.get(xy.getRelativeLocation(RIGHT).toString()));
                        break;
                    case NORTH_EAST:
                        pipe.addConnection(UP, pipes.get(xy.getRelativeLocation(UP).toString()));
                        pipe.addConnection(RIGHT, pipes.get(xy.getRelativeLocation(RIGHT).toString()));
                        break;
                    case NORTH_WEST:
                        pipe.addConnection(UP, pipes.get(xy.getRelativeLocation(UP).toString()));
                        pipe.addConnection(LEFT, pipes.get(xy.getRelativeLocation(LEFT).toString()));
                        break;
                    case SOUTH_WEST:
                        pipe.addConnection(DOWN, pipes.get(xy.getRelativeLocation(DOWN).toString()));
                        pipe.addConnection(LEFT, pipes.get(xy.getRelativeLocation(LEFT).toString()));
                        break;
                    case SOUTH_EAST:
                        pipe.addConnection(DOWN, pipes.get(xy.getRelativeLocation(DOWN).toString()));
                        pipe.addConnection(RIGHT, pipes.get(xy.getRelativeLocation(RIGHT).toString()));
                        break;
                    default:
                        System.out.println("weird type:\t" + pipe.getXy());
                }
            }
            // Check for anything with too many connectios
            pipes.values().stream().filter(pipe -> pipe.getConnections().size() > 2).forEach(pipe -> System.out.printf("%s\t%d\n", pipe.getXy(), pipe.getConnections().size()));
        } catch (
                Exception e) {
            System.out.println("Exception occurred: " + e.getMessage());
        }
    }


//    private void makePipeConnection(XYPoint xy, PipeType type) {
//        // Make a new pipe segment & connect it
//        XYPoint location = pipes.containsKey(xy) ? pipes.get(xy) :
//    }
}
