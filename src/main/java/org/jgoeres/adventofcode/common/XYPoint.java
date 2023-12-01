package org.jgoeres.adventofcode.common;

import java.util.Objects;

import static org.jgoeres.adventofcode.common.Direction8Way.*;

public class XYPoint {
    public static final XYPoint ORIGIN_XY = new XYPoint(0, 0);

    private int x = 0;
    private int y = 0;

    public XYPoint(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public XYPoint() {
        // Create at 0,0
    }

    public void set(int x, int y) {
        setX(x);
        setY(y);
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public XYPoint getRelativeLocation(Direction8Way directionURDL) {
        return getRelativeLocation(1, directionURDL);
    }

    public XYPoint getRelativeLocation(int numSteps, Direction8Way directionURDL) {
        switch (directionURDL) {
            case UP:
                // Default negative-Y direction is UP
                return (new XYPoint(getX(), getY() - numSteps));
            case RIGHT:
                return (new XYPoint(getX() + numSteps, getY()));
            case DOWN:
                // Default positive-Y direction is DOWN
                return (new XYPoint(getX(), getY() + numSteps));
            case LEFT:
                return (new XYPoint(getX() - numSteps, getY()));
        }
        return null;
    }

    public XYPoint getRelativeLocation8Way(Direction8Way direction8Way) {
        return getRelativeLocation8Way(1, direction8Way);
    }

    public XYPoint getRelativeLocation8Way(int numSteps, Direction8Way direction8Way) {
        switch (direction8Way) {
            case UP:
                return (getRelativeLocation(numSteps, UP));
            case UP_RIGHT:
                return (getRelativeLocation(numSteps, UP).getRelativeLocation(numSteps, Direction8Way.RIGHT));
            case RIGHT:
                return (getRelativeLocation(numSteps, RIGHT));
            case DOWN_RIGHT:
                return (getRelativeLocation(numSteps, DOWN).getRelativeLocation(numSteps, Direction8Way.RIGHT));
            case DOWN:
                return (getRelativeLocation(numSteps, DOWN));
            case DOWN_LEFT:
                // Default negative-Y direction is UP
                return (getRelativeLocation(numSteps, DOWN).getRelativeLocation(numSteps, Direction8Way.LEFT));
            case LEFT:
                return (getRelativeLocation(numSteps, LEFT));
            case UP_LEFT:
                return (getRelativeLocation(numSteps, UP).getRelativeLocation(numSteps, Direction8Way.LEFT));
        }
        return null;
    }

    public XYPoint getRelativeLocationHexPointy(DirectionHexPointy directionHexPointy) {
        return getRelativeLocationHexPointy(1, directionHexPointy);
    }

    public XYPoint getRelativeLocationHexPointy(int numSteps, DirectionHexPointy directionHexPointy) {
        // NE, SE, NW, SW switch based on whether the y-coordinate is odd or even

        switch (directionHexPointy) {
            case E:
                return (new XYPoint(getX() + numSteps, getY()));
            case W:
                return (new XYPoint(getX() - numSteps, getY()));
            default:
                if (getY() % 2 == 0) {
                    // Y is *EVEN*
                    switch (directionHexPointy) {
                        case SE:
                            // Default positive-Y direction is DOWN
                            return (new XYPoint(getX() + numSteps, getY() + numSteps));
                        case NE:
                            // Default negative-Y direction is UP
                            return (new XYPoint(getX() + numSteps, getY() - numSteps));
                        case SW:
                            // Default positive-Y direction is DOWN
                            return (new XYPoint(getX(), getY() + numSteps));
                        case NW:
                            // Default negative-Y direction is UP
                            return (new XYPoint(getX(), getY() - numSteps));
                    }
                } else {
                    // Y is *ODD*
                    switch (directionHexPointy) {
                        case SE:
                            // Default positive-Y direction is DOWN
                            return (new XYPoint(getX(), getY() + numSteps));
                        case NE:
                            // Default negative-Y direction is UP
                            return (new XYPoint(getX(), getY() - numSteps));
                        case SW:
                            // Default positive-Y direction is DOWN
                            return (new XYPoint(getX() - numSteps, getY() + numSteps));
                        case NW:
                            // Default negative-Y direction is UP
                            return (new XYPoint(getX() - numSteps, getY() - numSteps));

                    }
                }
        }
        return null;    // We should never get here
    }

    public void moveRelative(int numSteps, DirectionURDL directionURDL) {
        switch (directionURDL) {
            case UP:
                // Default negative-Y direction is UP
                setY(y - numSteps);
                break;
            case RIGHT:
                setX(x + numSteps);
                break;
            case DOWN:
                // Default positive-Y direction is DOWN
                setY(y + numSteps);
                break;
            case LEFT:
                setX(x - numSteps);
                break;
        }
    }

    @Override
    public String toString() {
        return (getX() + ", " + getY());
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof XYPoint))
            return false;
        if (obj == this)
            return true;
        // Two points are equivalent if they have the same coordinates
        return ((this.x == ((XYPoint) obj).getX())
                && (this.y == ((XYPoint) obj).getY()));
    }

    @Override
    public int hashCode() {
        return (Objects.hash(x, y));
    }
}
