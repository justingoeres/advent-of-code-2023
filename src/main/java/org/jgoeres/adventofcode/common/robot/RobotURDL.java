package org.jgoeres.adventofcode.common.robot;

import org.jgoeres.adventofcode.common.DirectionURDL;
import org.jgoeres.adventofcode.common.Rotation;
import org.jgoeres.adventofcode.common.XYPoint;

import static org.jgoeres.adventofcode.common.DirectionURDL.UP;

public class RobotURDL extends AbstractRobot {
    private DirectionURDL facing;

    public RobotURDL(XYPoint location) {
        super(location);
        this.facing = UP;   // Robot starts facing UP.
    }

    public RobotURDL(XYPoint location, DirectionURDL facing) {
        super(location);
        this.facing = facing;
    }

    @Override
    public void turnRobot(Rotation rotation) {
        facing = facing.rotate(rotation);
    }

    public void moveRobot(int numSteps) {
        switch (getFacing()) {
            case UP:
                // Default URDL robot has negative-Y as UP
                location.setY(location.getY() - numSteps);
                break;
            case RIGHT:
                location.setX(location.getX() + numSteps);
                break;
            case DOWN:
                // Default URDL robot has positive-Y as DOWN
                location.setY(location.getY() + numSteps);
                break;
            case LEFT:
                location.setX(location.getX() - numSteps);
                break;
        }
    }


    public DirectionURDL getFacing() {
        return facing;
    }
}
