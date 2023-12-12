package org.jgoeres.adventofcode2023.Day10;

import lombok.Getter;
import lombok.Setter;
import org.jgoeres.adventofcode.common.DirectionURDL;
import org.jgoeres.adventofcode.common.Utils.Pair;
import org.jgoeres.adventofcode.common.XYPoint;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.*;
import java.util.stream.Collectors;

import static org.jgoeres.adventofcode.common.DirectionURDL.*;
import static org.jgoeres.adventofcode2023.Day10.PipeType.*;
import static org.jgoeres.adventofcode2023.Day10.ThingFound.*;

public class Day10Service {
    public boolean DEBUG = false;

    private Map<String, Pipe> pipes = new HashMap();
    private Map<String, Pipe> theLoop = new HashMap<>();
    private Pipe start;
    public static final char EMPTY_CHAR = '.';
    private Integer xMax;
    private Integer yMax;

    public Day10Service(String pathToFile) {
        loadInputs(pathToFile);
    }

    public Day10Service(String pathToFile, boolean debug) {
        loadInputs(pathToFile);
        DEBUG = debug;
    }

    @Getter
    @Setter
    public static class Answers {
        Long partA;
        Long partB;
    }

    public Answers doPuzzle() {
        final Answers answers = new Answers();
        answers.setPartA(doPartA());
        answers.setPartB(doPartB());
        return answers;
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
        theLoop.putAll(visited.stream().collect(Collectors.toMap(pipe -> pipe.getXy().toString(), pipe -> pipe)));
        System.out.println("Day 10A: Furthest distance from Start = " + stepCount);
        return stepCount;
    }


    public long doPartB() {
        System.out.println("=== DAY 10B ===");
//        printTheLoop();

        /**
         * Figure out whether you have time to search for the nest by calculating the area within the loop.
         * How many tiles are enclosed by the loop?
         **/

        Boolean done = false;
        XYPoint current = start.getXy();

        // Pick a direction to start
        DirectionURDL facing;
        switch (start.getType()) {
            case HORIZ:
            case NORTH_EAST:
            case SOUTH_EAST: // my 'start' is one of these
                facing = RIGHT;
                break;
            case NORTH_WEST:
            case SOUTH_WEST:
                facing = LEFT;
                break;
            case VERT:
                facing = UP;
                break;
            default:
                facing = UP;
                // this will never happen; 'start' is always something
        }

        // Now we travel.
        // As we go, look left and right. One of those sides will (eventually) see
        // a BOUNDARY. When that happens, we know that side is OUTSIDE.

        final Set<String> onLeft = new HashSet<>();
        final Set<String> onRight = new HashSet<>();
        DirectionURDL insideOn = LEFT;

        Set<String> visited = new HashSet<>();
        while (!done) {
            visited.add(current.toString());   // Record that we were here
            final ThingFound foundOnLeft = lookInDirection(current, getLookDirection(facing, LEFT), onLeft);
            final ThingFound foundOnRight = lookInDirection(current, getLookDirection(facing, RIGHT), onRight);

            if (foundOnLeft == BOUNDARY) {
                // If we ever see a Boundary on the Left, then the Right is Inside
                insideOn = RIGHT;
            }

            // Now move to the next segment
            final Pipe currentPipe = theLoop.get(current.toString());
            // Turn if we need to, and look again
            switch (currentPipe.getType()) {
                case NORTH_EAST:
                    facing = (facing == LEFT) ? UP : RIGHT;
                    lookInDirection(current, getLookDirection(facing, LEFT), onLeft);
                    lookInDirection(current, getLookDirection(facing, RIGHT), onRight);
                    break;
                case NORTH_WEST:
                    facing = (facing == RIGHT) ? UP : LEFT;
                    lookInDirection(current, getLookDirection(facing, LEFT), onLeft);
                    lookInDirection(current, getLookDirection(facing, RIGHT), onRight);
                    break;
                case SOUTH_EAST:
                    facing = (facing == LEFT) ? DOWN : RIGHT;
                    lookInDirection(current, getLookDirection(facing, LEFT), onLeft);
                    lookInDirection(current, getLookDirection(facing, RIGHT), onRight);
                    break;
                case SOUTH_WEST:
                    facing = (facing == RIGHT) ? DOWN : LEFT;
                    lookInDirection(current, getLookDirection(facing, LEFT), onLeft);
                    lookInDirection(current, getLookDirection(facing, RIGHT), onRight);
                    break;
                default:
                    // We don't change direction, don't need to look
                    break;
            }
            // Move
            current.moveRelative(1, facing);
            // Stop when we get back to the start
            done = (visited.contains(current.toString()));
        }
        final long result = (insideOn == LEFT) ? onLeft.size() : onRight.size();
        System.out.printf("Day 10B: Number of empty spaces found inside the loop (on the %s) = %d", insideOn, result);
        return result;
    }

    private DirectionURDL getLookDirection(DirectionURDL facing, DirectionURDL look) {
        switch (facing) {
            case UP:
                return (look == LEFT) ? LEFT : RIGHT;
            case RIGHT:
                return (look == LEFT) ? UP : DOWN;
            case DOWN:
                return (look == LEFT) ? RIGHT : LEFT;
            case LEFT:
                return (look == LEFT) ? DOWN : UP;
        }
        return null;    // This won't happen
    }

    private ThingFound lookInDirection(final XYPoint xy0, final DirectionURDL looking, final Set<String> seen) {

        XYPoint xy = new XYPoint(xy0.getX(), xy0.getY());

        // Scan from xy0 in the specified direction, and return
        // what we see first: a boundary or a pipe
        while (true) {      // go until we hit the pipe or a boundary
            xy.moveRelative(1, looking);
            if (theLoop.containsKey(xy.toString())) {
                // If we see a part of the loop, return that
                return PIPE;
            } else if (xy.getX() >= xMax
                    || xy.getX() < 0
                    || xy.getY() >= yMax
                    || xy.getY() < 0) {
                // If we've hit the boundary
                return BOUNDARY;
            } else {
                // If we see an EMPTY, add it to what we've seen and keep going
                seen.add(xy.toString());
            }
        }
    }

    // load inputs line-by-line and extract fields
    private void loadInputs(String pathToFile) {
        pipes.clear();
        try (BufferedReader br = new BufferedReader(new FileReader(pathToFile))) {
            String line;
            Integer y = 0;
            while ((line = br.readLine()) != null) {
                xMax = line.length();
                // process the line.
                for (int x = 0; x < line.length(); x++) {
                    char pipeChar = line.charAt(x);
                    XYPoint xy = new XYPoint(x, y);
                    if (pipeChar != EMPTY_CHAR) {
                        // First, just create the pipes but don't attach them
                        PipeType type = PipeType.fromChar(pipeChar);
                        Pipe newPipe = new Pipe(type, xy);
                        pipes.put(xy.toString(), newPipe);
                        if (type == START) start = newPipe;
                    }
                }
                y++;
            }
            yMax = y;
            // Now that we've got all the pipes, connect them up to each other
            for (Pipe pipe : pipes.values()) {
                XYPoint xy = pipe.getXy();
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
            // Identify the type of START
            start.identifyType();
            // Check for anything with too many connections
            pipes.values().stream().filter(pipe -> pipe.getConnections().size() > 2).forEach(pipe -> System.out.printf("%s\t%d\n", pipe.getXy(), pipe.getConnections().size()));
        } catch (
                Exception e) {
            System.out.println("Exception occurred: " + e.getMessage());
        }
    }

    private void printTheLoop() {
        System.out.printf("\n\n");
        XYPoint xy = new XYPoint();
        for (int y = 0; y < yMax; y++) {
            String line = "";
            for (int x = 0; x < xMax; x++) {
                xy.set(x, y);
                Character out;
                if (theLoop.containsKey(xy.toString())) {
                    out = theLoop.get(xy.toString()).getType().label;
                } else {
                    out = ' ';    // print a space if nothing is there
                }
                line += out;
            }
            System.out.println(line);
        }
    }
}

enum ThingFound {
    BOUNDARY,
    PIPE
}

