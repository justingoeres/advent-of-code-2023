package org.jgoeres.adventofcode.common.intcode;

public class Parameter {
    private ParamMode mode;
    private Long value;

    public Parameter(ParamMode mode, Long value) {
        this.mode = mode;
        this.value = value;
    }

    public ParamMode getMode() {
        return mode;
    }

    public void setMode(ParamMode mode) {
        this.mode = mode;
    }

    public Long getValue() {
        return value;
    }

    public void setValue(Long value) {
        this.value = value;
    }
}
