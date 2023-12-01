package org.jgoeres.adventofcode.common;

import java.util.HashMap;
import java.util.Map;

import static org.jgoeres.adventofcode.common.Rotation.CLOCKWISE;
import static org.jgoeres.adventofcode.common.Rotation.COUNTERCLOCKWISE;

public enum DirectionHexPointy {
    // The "pointy" orientation where a point is at the bottom 
    // (as opposed to a "flat" where the bottom edge is horizontal)
    E("E"),
    SE("SE"),
    SW("SW"),
    W("W"),
    NW("NW"),
    NE("NE");
    private final String directionString;

    //****** Reverse Lookup Implementation************//

    //Lookup table
    private static final Map<String, DirectionHexPointy> lookup = new HashMap<>();

    //Populate the lookup table on loading time
    static {
        for (DirectionHexPointy dir : DirectionHexPointy.values()) {
            lookup.put(dir.getDirectionString(), dir);
        }
    }

    //This method can be used for reverse lookup purpose
    public static DirectionHexPointy get(String directionString) {
        return lookup.get(directionString);
    }

    DirectionHexPointy(String directionString) {
        this.directionString = directionString;
    }

    public String getDirectionString() {
        return directionString;
    }

    public DirectionHexPointy opposite() {
        switch (get(directionString)) {
            case NE:
                return SW;
            case E:
                return W;
            case SE:
                return NW;
            case SW:
                return NE;
            case W:
                return E;
            case NW:
                return SE;
        }
        // We can never actually get here
        return null;
    }

    public DirectionHexPointy rotate(Rotation rotation) {
        int newDirection = this.ordinal();
        if (rotation == CLOCKWISE) {
            newDirection += 1;
            if (newDirection >= this.values().length) {
                // wrap it
                newDirection -= this.values().length;
            }
        }
        if (rotation == COUNTERCLOCKWISE) {
            newDirection -= 1;
            if (newDirection < 0) {
                // wrap it the other way
                newDirection += this.values().length;
            }
        }
        return DirectionHexPointy.values()[newDirection];
    }
}
