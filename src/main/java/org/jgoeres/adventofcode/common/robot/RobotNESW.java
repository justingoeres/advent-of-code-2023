package org.jgoeres.adventofcode.common.robot;

import org.jgoeres.adventofcode.common.DirectionNESW;
import org.jgoeres.adventofcode.common.Rotation;
import org.jgoeres.adventofcode.common.XYPoint;

public class RobotNESW extends AbstractRobot {
    private DirectionNESW facing;

    public RobotNESW(XYPoint location) {
        super(location);
    }

    public RobotNESW(XYPoint location, DirectionNESW facing) {
        super(location);
        this.facing = facing;
    }

    public void stepRobot(DirectionNESW directionNESW) {
        int DEFAULT_DISTANCE = 1;
        moveRobot(DEFAULT_DISTANCE, directionNESW);
    }

    @Override
    public void moveRobot(int numSteps) {
        moveRobot(numSteps, facing);
    }

    public void moveRobot(int numSteps, DirectionNESW directionNESW) {
        switch (directionNESW) {
            case NORTH:
                location = new XYPoint(location.getX(), location.getY() + numSteps);
                break;
            case EAST:
                location = new XYPoint(location.getX() + numSteps, location.getY());
                break;
            case SOUTH:
                location = new XYPoint(location.getX(), location.getY() - numSteps);
                break;
            case WEST:
                location = new XYPoint(location.getX() - numSteps, location.getY());
                break;
        }
    }

    public XYPoint getRelativeLocation(DirectionNESW directionNESW) {
        return getRelativeLocation(1, directionNESW);
    }

    public XYPoint getRelativeLocation(int numSteps, DirectionNESW directionNESW) {
        switch (directionNESW) {
            case NORTH:
                return (new XYPoint(location.getX(), location.getY() + numSteps));
            case EAST:
                return (new XYPoint(location.getX() + numSteps, location.getY()));
            case SOUTH:
                return (new XYPoint(location.getX(), location.getY() - numSteps));
            case WEST:
                return (new XYPoint(location.getX() - numSteps, location.getY()));
        }
        return null;
    }

    @Override
    public void turnRobot(Rotation rotation) {

    }
}
