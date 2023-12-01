package org.jgoeres.adventofcode.common;

import java.util.Objects;

public class XYZWPoint extends XYZPoint {
    private int w = 0;

    public XYZWPoint(int x, int y, int z, int w) {
        super(x, y, z);
        this.w = w;
    }

    public XYZWPoint(int x, int y, int z) {
        super(x, y, z);
    }

    public XYZWPoint() {
        // Create at 0, 0, 0,0
    }

    public int getW() {
        return w;
    }

    public void setW(int w) {
        this.w = w;
    }

    @Override
    public String toString() {
        return (getX() + ", " + getY() + ", " + getZ() + ", " + getW());
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof XYZWPoint))
            return false;
        if (obj == this)
            return true;
        // Two points are equivalent if they have the same coordinates
        return ((this.getX() == ((XYZWPoint) obj).getX())
                && (this.getY() == ((XYZWPoint) obj).getY())
                && (this.getZ() == ((XYZWPoint) obj).getZ())
                && (this.getW() == ((XYZWPoint) obj).getW()));
    }

    @Override
    public int hashCode() {
        // Make the hash code things like (3,4,5) -> 500040003
        return (Objects.hash(getX(), getY(), getZ(), w));
    }
}
