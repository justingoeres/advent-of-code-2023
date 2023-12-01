package org.jgoeres.adventofcode.common.intcode;

public enum ParamMode {
    POSITION(0),
    IMMEDIATE(1),
    RELATIVE(2);

    private int modeInt;

    ParamMode(int modeInt) {
        this.modeInt = modeInt;
    }

    public static ParamMode fromInt(long x) {
        switch (Math.round(x)) {
            case 0:
                return POSITION;
            case 1:
                return IMMEDIATE;
            case 2:
                return RELATIVE;
        }
        return null;
    }
}
