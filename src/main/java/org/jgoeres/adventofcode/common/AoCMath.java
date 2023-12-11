package org.jgoeres.adventofcode.common;

public abstract class AoCMath {
    public static final XYPoint ORIGIN = new XYPoint(0, 0);

    public static Long manhattanDistance(XYPoint p0, XYPoint p1) {
        long xDistance = Math.abs(p0.getX() - p1.getX());
        long yDistance = Math.abs(p0.getY() - p1.getY());

        return (xDistance + yDistance);
    }

    public static Long manhattanDistance(XYZPoint p0, XYZPoint p1) {
        long xDistance = Math.abs(p0.getX() - p1.getX());
        long yDistance = Math.abs(p0.getY() - p1.getY());
        long zDistance = Math.abs(p0.getZ() - p1.getZ());
        return (xDistance + yDistance + zDistance);
    }

    public static long lcm(long number1, long number2) {
        if (number1 == 0 || number2 == 0) {
            return 0;
        }
        long absNumber1 = Math.abs(number1);
        long absNumber2 = Math.abs(number2);
        long absHigherNumber = Math.max(absNumber1, absNumber2);
        long absLowerNumber = Math.min(absNumber1, absNumber2);
        long lcm = absHigherNumber;
        while (lcm % absLowerNumber != 0) {
            lcm += absHigherNumber;
        }
        return lcm;
    }
}
