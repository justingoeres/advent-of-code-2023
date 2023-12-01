package org.jgoeres.adventofcode.common;

public class RotationStep {
    private final XYZPoint.Axis axis;
    private final Integer times;

    public RotationStep(XYZPoint.Axis axis) {
        this.axis = axis;
        this.times = 1;
    }

    public RotationStep(XYZPoint.Axis axis, Integer times) {
        this.axis = axis;
        this.times = times;
    }

    public XYZPoint.Axis getAxis() {
        return axis;
    }

    public Integer getTimes() {
        return times;
    }
}
