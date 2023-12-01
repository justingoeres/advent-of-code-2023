package org.jgoeres.adventofcode.common.intcode;


public interface IOpCode {
    boolean execute(Instruction instruction);
}
