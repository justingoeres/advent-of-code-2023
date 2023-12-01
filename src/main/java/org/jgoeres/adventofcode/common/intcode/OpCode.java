package org.jgoeres.adventofcode.common.intcode;

public enum OpCode {
    ADD(1L,3),
    MULTIPLY(2L,3),
    INPUT(3L,1),
    OUTPUT(4L,1),
    JUMP_IF_TRUE(5L,2),
    JUMP_IF_FALSE(6L,2),
    LESS_THAN(7L,3),
    EQUALS(8L,3),
    RELATIVE_BASE(9L,1),
    HALT(99L,0);

    private long value;
    private int numArgs;

    OpCode(long value, int numArgs) {
        this.value = value;
        this.numArgs = numArgs;
    }

    public long getValue(){
        return value;
    }

    public int getNumArgs(){
        return numArgs;
    }

    public static OpCode fromInt(long x) {
        switch (Math.round(x)) {
            case 1:
                return ADD;
            case 2:
                return MULTIPLY;
            case 3:
                return INPUT;
            case 4:
                return OUTPUT;
            case 5:
                return JUMP_IF_TRUE;
            case 6:
                return JUMP_IF_FALSE;
            case 7:
                return LESS_THAN;
            case 8:
                return EQUALS;
            case 9:
                return RELATIVE_BASE;
            case 99:
                return HALT;
        }
        return null;
    }
}
