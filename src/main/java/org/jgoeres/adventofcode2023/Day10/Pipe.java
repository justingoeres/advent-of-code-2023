package org.jgoeres.adventofcode2023.Day10;

import lombok.Getter;
import lombok.Setter;
import org.jgoeres.adventofcode.common.DirectionURDL;
import org.jgoeres.adventofcode.common.XYPoint;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import static org.jgoeres.adventofcode.common.DirectionURDL.*;
import static org.jgoeres.adventofcode2023.Day10.PipeType.*;


@Getter
@Setter
public class Pipe {
    PipeType type;
    final XYPoint xy;
    final private Map<DirectionURDL, Pipe> connections = new HashMap<>();

    private static final Map<PipeType, Set<DirectionURDL>> VALID_CONNECTIONS = Map.of(
            VERT, Set.of(UP, DOWN),
            HORIZ, Set.of(LEFT, RIGHT),
            NORTH_EAST, Set.of(UP, RIGHT),
            NORTH_WEST, Set.of(UP, LEFT),
            SOUTH_WEST, Set.of(DOWN, LEFT),
            SOUTH_EAST, Set.of(DOWN, RIGHT),
            START, Set.of(UP, DOWN, RIGHT, LEFT));  // START can connect to anything


    public Pipe(PipeType type, XYPoint xy) {
        this.type = type;
        this.xy = xy;
    }

    public void addConnection(DirectionURDL direction, Pipe otherPipe) {
        if (otherPipe == null) return;   // dead end, just bail

        // Only connect if it's not already connected to something
        //    ... and if it's a direction this type is allowed to connect
        if (!connections.containsKey(direction)
                && VALID_CONNECTIONS.get(this.type).contains(direction)) {
            connections.putIfAbsent(direction, otherPipe);
            // Also add the corresponding connection the other way
            otherPipe.addConnection(direction.opposite(), this);
        }
    }

    public void identifyType() {
        Set<DirectionURDL> connectionDirs = getConnections().keySet();
        for (Map.Entry<PipeType, Set<DirectionURDL>> valid : VALID_CONNECTIONS.entrySet()) {
            if (valid.getValue().size() == 2 && valid.getValue().containsAll(connectionDirs)) {
                // If the connections match the type, set the type
                this.setType(valid.getKey());
                return;
            }
        }
    }

    public Pipe getConnection(DirectionURDL direction) {
        return connections.get(direction);
    }

//    public Pipe(PipeType type, XYPoint xy) {
//        this.type = type;
//        this.xy = xy;
//        switch (type) {
//            case VERT:
//                // top first
//                this.up = xy.getRelativeLocation(UP);
//                this.down = xy.getRelativeLocation(DOWN);
//                break;
//            case HORIZ:
//                // left first
//                this.left = xy.getRelativeLocation(LEFT);
//                this.right = xy.getRelativeLocation(RIGHT);
//                break;
//            case NORTH_EAST:
//                this.up = xy.getRelativeLocation(UP);
//                this.right = xy.getRelativeLocation(RIGHT);
//                break;
//            case NORTH_WEST:
//                this.up = xy.getRelativeLocation(UP);
//                this.left = xy.getRelativeLocation(LEFT);
//                break;
//            case SOUTH_WEST:
//                this.down = xy.getRelativeLocation(DOWN);
//                this.left = xy.getRelativeLocation(LEFT);
//                break;
//            case SOUTH_EAST:
//                this.down = xy.getRelativeLocation(DOWN);
//                this.right = xy.getRelativeLocation(RIGHT);
//                break;
//        }
//    }
}

enum PipeType {
    EMPTY('.'),
    START('S'),
    VERT('|'),
    HORIZ('-'),
    NORTH_EAST('L'),
    NORTH_WEST('J'),
    SOUTH_WEST('7'),
    SOUTH_EAST('F');

    public final char label;

    private PipeType(char label) {
        this.label = label;
    }

    private static final Map<Character, PipeType> BY_LABEL = new HashMap<>();

    static {
        for (PipeType e : values()) {
            BY_LABEL.put(e.label, e);
        }
    }

    // ... fields, constructor, methods

    public static PipeType fromChar(Character label) {
        return BY_LABEL.get(label);
    }
}
