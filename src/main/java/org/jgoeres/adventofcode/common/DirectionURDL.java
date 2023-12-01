package org.jgoeres.adventofcode.common;

import java.util.HashMap;
import java.util.Map;

import static org.jgoeres.adventofcode.common.Rotation.CLOCKWISE;
import static org.jgoeres.adventofcode.common.Rotation.COUNTERCLOCKWISE;

public enum DirectionURDL {
    UP("U"),
    RIGHT("R"),
    DOWN("D"),
    LEFT("L");

    private final String directionString;

    //****** Reverse Lookup Implementation************//

    //Lookup table
    private static final Map<String, DirectionURDL> lookup = new HashMap<>();

    //Populate the lookup table on loading time
    static {
        for (DirectionURDL dir : DirectionURDL.values()) {
            lookup.put(dir.getDirectionString(), dir);
        }
    }

    //This method can be used for reverse lookup purpose
    public static DirectionURDL get(String directionString) {
        return lookup.get(directionString);
    }

    DirectionURDL(String directionString) {
        this.directionString = directionString;
    }

    public String getDirectionString() {
        return directionString;
    }

    public DirectionURDL rotate(Rotation rotation) {
        int newDirection = this.ordinal();
        DirectionURDL newDirectionURDLEnum = null;
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
        return DirectionURDL.values()[newDirection];
    }
}
