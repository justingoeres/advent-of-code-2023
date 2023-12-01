package org.jgoeres.adventofcode.common;

public class Area {
    private final XYPoint upperLeft;
    private final XYPoint lowerRight;

    public Area(XYPoint upperLeft, XYPoint lowerRight) {
        this.upperLeft = upperLeft;
        this.lowerRight = lowerRight;
    }

    public Boolean contains(XYPoint xy) {
        final Boolean withinX = xContains(xy.getX());
        final Boolean withinY = yContains(xy.getY());
        return withinX && withinY;
    }

    public Boolean xContains(Integer x) {
        return (upperLeft.getX() <= x
                && x <= lowerRight.getX());
    }

    public Boolean yContains(Integer y) {
        return (lowerRight.getY() <= y
                && y <= upperLeft.getY());
    }

    public XYPoint getUpperLeft() {
        return upperLeft;
    }

    public XYPoint getLowerRight() {
        return lowerRight;
    }
}
