package org.jgoeres.adventofcode.common;

import java.util.HashMap;
import java.util.Map;

public enum Rotation
{
    CLOCKWISE("R"),
    COUNTERCLOCKWISE("L");

    private final String rotationString;

    //****** Reverse Lookup Implementation************//

    //Lookup table
    private static final Map<String, Rotation> lookup = new HashMap<>();

    //Populate the lookup table on loading time
    static
    {
        for(Rotation dir : Rotation.values())
        {
            lookup.put(dir.getRotationString(), dir);
        }
    }

    //This method can be used for reverse lookup purpose
    public static Rotation get(String directionString)
    {
        return lookup.get(directionString);
    }
    Rotation(String rotationString) {
        this.rotationString = rotationString;
    }

    public String getRotationString() {
        return rotationString;
    }

}
