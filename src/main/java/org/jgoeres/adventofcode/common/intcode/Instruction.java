package org.jgoeres.adventofcode.common.intcode;

import java.util.ArrayList;

public class Instruction {
    private OpCode opCode;

    private ArrayList<Parameter> params;

    public Instruction(OpCode opCode, ArrayList<Parameter> params) {
        this.opCode = opCode;
        this.params = params;
    }

    public OpCode getOpCode() {
        return opCode;
    }

    public Parameter getParam(int index) {
        return params.get(index);
    }
}
