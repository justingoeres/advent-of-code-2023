package org.jgoeres.adventofcode.common;

import java.util.HashMap;
import java.util.Map;

import static org.jgoeres.adventofcode.common.Rotation.CLOCKWISE;
import static org.jgoeres.adventofcode.common.Rotation.COUNTERCLOCKWISE;

public enum DirectionNESW {
    NORTH(1L),
    EAST(4L),
    SOUTH(2L),
    WEST(3L);

    private final Long directionInt;

    //****** Reverse Lookup Implementation************//

    //Lookup table
    private static final Map<Long, DirectionNESW> lookup = new HashMap<Long, DirectionNESW>();

    //Populate the lookup table on loading time
    static {
        for (DirectionNESW dir : DirectionNESW.values()) {
            lookup.put(dir.getDirectionInt(), dir);
        }
    }

    //This method can be used for reverse lookup purpose
    public static DirectionNESW get(String directionString) {
        return lookup.get(directionString);
    }

    DirectionNESW(Long directionInt) {
        this.directionInt = directionInt;
    }

    public Long getDirectionInt() {
        return directionInt;
    }

    public DirectionNESW opposite(){
        switch (this) {
            case NORTH:
                return SOUTH;
            case SOUTH:
                return NORTH;
            case EAST:
                return WEST;
            case WEST:
                return EAST;
        }
        return null;
    }

    public DirectionNESW rotate(Rotation rotation) {
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
        return DirectionNESW.values()[newDirection];
    }
}
