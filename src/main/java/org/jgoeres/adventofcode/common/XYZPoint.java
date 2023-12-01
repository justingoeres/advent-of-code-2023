package org.jgoeres.adventofcode.common;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;

public class XYZPoint extends XYPoint {
    public static final XYZPoint ORIGIN_XYZ = new XYZPoint(0, 0, 0);

    private int z = 0;

    public XYZPoint(int x, int y, int z) {
        super(x, y);
        this.z = z;
    }

    public XYZPoint() {
        // Create at 0, 0, 0
    }

    public int getZ() {
        return z;
    }

    public void setZ(int z) {
        this.z = z;
    }

    @Override
    public String toString() {
        return (getX() + ", " + getY() + ", " + getZ());
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof XYZPoint)) {
            return false;
        }
        if (obj == this) {
            return true;
        }
        // Two points are equivalent if they have the same coordinates
        return ((this.getX() == ((XYZPoint) obj).getX())
                && (this.getY() == ((XYZPoint) obj).getY())
                && (this.getZ() == ((XYZPoint) obj).getZ()));
    }

    @Override
    public int hashCode() {
        // Make the hash code things like (3,4,5) -> 500040003
        return (Objects.hash(getX(), getY(), z));
    }

    public XYZPoint rotate(final Axis axis) {
        return rotate(axis, 1);
    }

    public XYZPoint rotate(final Axis axis, final Integer times) {
        // return this point, rotated 90 degrees about the given axis, the given number of times
        for (int i = 0; i < times; i++) {
            Integer x = this.getX();
            Integer y = this.getY();
            Integer z = this.getZ();
            switch (axis) {
                case X_AXIS:
                    this.setY(-1 * z);
                    this.setZ(y);
                    break;
                case Y_AXIS:
                    this.setX(-1 * z);
                    this.setZ(x);
                    break;
                case Z_AXIS:
                    this.setX(-1 * y);
                    this.setY(x);
                    break;
                case NONE:
                    return this;
            }
        }
        return this;
    }

    public XYZPoint translate(XYZPoint xyz) {
        // Return a new point translated by the given vector
        this.setX(this.getX() + xyz.getX());
        this.setY(this.getY() + xyz.getY());
        this.setZ(this.getZ() + xyz.getZ());
        return this;
    }

    public static XYZPoint getXYZToReference(final XYZPoint xyz, XYZPoint reference) {
        return (new XYZPoint(
                xyz.getX() - reference.getX(),
                xyz.getY() - reference.getY(),
                xyz.getZ() - reference.getZ()));
    }

    public static List<XYZPoint> getXYZToReference(final Collection<XYZPoint> xyzPoints,
                                                   final XYZPoint reference) {
        // Return a list of XYZPoints translated so that they're relative to the 'reference' point
        final List<XYZPoint> xyzToReference = new ArrayList<>();
        xyzPoints.stream().forEach(xyz -> xyzToReference.add(getXYZToReference(xyz, reference)));
        return xyzToReference;
    }

    public enum Axis {
        X_AXIS,
        Y_AXIS,
        Z_AXIS,
        NONE
    }
}
