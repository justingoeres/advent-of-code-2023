package org.jgoeres.adventofcode.common.intcode;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.*;

public class CPU {
    private static final Map<OpCode, Runnable> commands = new HashMap<>();

    long pc;
    Long relativeBase;
    protected Queue<Long> inputQueue = new LinkedList<Long>();
    protected HashMap<Long, Long> programCode;
    HashMap<Long, Long> programCodeOriginal;
    protected Long lastOutput = 0L;


    protected Instruction nextInstruction = null;
    protected boolean waitingForInput = false;
    private boolean outputReady = false;
    private boolean halted = false;


    public CPU(HashMap<Long, Long> programCode) {
        this.programCodeOriginal = programCode;
        reset();
    }

    public CPU(String inputFile) {
        loadInputs(inputFile);
    }

    public void reset() {
        pc = 0;
        relativeBase = 0L;
        lastOutput = 0L;
        waitingForInput = false;
        outputReady = false;
        halted = false;
        inputQueue.clear();
        programCode = (HashMap<Long, Long>) programCodeOriginal.clone();
    }

    // Create a map of OpCodes to functors that implement them
    protected HashMap<OpCode, IOpCode> opCodeFunctorMap() {
        HashMap<OpCode, IOpCode> map = new HashMap<>();

        // Day 2
        map.put(OpCode.ADD, (instruction) -> add(instruction));
        map.put(OpCode.MULTIPLY, (instruction) -> multiply(instruction));
        map.put(OpCode.HALT, (instruction) -> halt(instruction));
        // Day 5A
        map.put(OpCode.INPUT, (instruction) -> input(instruction));
        map.put(OpCode.OUTPUT, (instruction) -> output(instruction));
        // Day 5B
        map.put(OpCode.JUMP_IF_TRUE, (instruction) -> jumpIfTrue(instruction));
        map.put(OpCode.JUMP_IF_FALSE, (instruction) -> jumpIfFalse(instruction));
        map.put(OpCode.LESS_THAN, (instruction) -> lessThan(instruction));
        map.put(OpCode.EQUALS, (instruction) -> equals(instruction));
        map.put(OpCode.RELATIVE_BASE, (instruction) -> relativeBase(instruction));

        return map;
    }

    public boolean executeNext() {
        // Use the program counter to read the current OpCode and execute it.
        // Return true to continue, false to halt.
        if (!waitingForInput) {
            // If we're NOT waiting for input, get the next instruction
            nextInstruction = decodeInstruction();
        } else {
            // If we're waiting for input, stay on the input instruction to see if we have any
        }
        // Execute the next instruction
        boolean keepGoing = opCodeFunctorMap().get(nextInstruction.getOpCode()).execute(nextInstruction);
        return keepGoing;
    }

    public void setValueAtPosition(Long position, Long value) {
        programCode.put(position, value);
    }

    private Long getValueAtPCAndAdvance() {
        Long value = getValueAtPosition(pc);
        pc++;
        return value;
    }

    public Long getValueAtPosition(Long position) {
        Long result = 0L;
        try {
            if (programCode.containsKey(position)) {
                // If this location exists in memory, retrieve it
                result = programCode.get(position);
            } else {
                // return zero
                result = 0L;
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return result;
    }

    public Instruction decodeInstruction() {
        //  1002,4,3,4,33
        //
        //ABCDE
        // 1002
        //
        //DE - two-digit opcode,      02 == opcode 2
        // C - mode of 1st parameter,  0 == position mode
        // B - mode of 2nd parameter,  1 == immediate mode
        // A - mode of 3rd parameter,  0 == position mode,
        //                                  omitted due to being a leading zero
        //This instruction multiplies its first two parameters. The first parameter,
        // 4 in position mode, works like it did before - its value is the value
        // stored at address 4 (33). The second parameter, 3 in immediate mode,
        // simply has value 3. The result of this operation, 33 * 3 = 99, is
        // written according to the third parameter, 4 in position mode, which
        // also works like it did before - 99 is written to address 4.

        // Get the raw instruction value from the pc.
        Long instr = getValueAtPCAndAdvance();

        // Decode it into opcode & parameter modes
        long opCodeInt = instr % 100;    // last two digits are opcode
        OpCode opCode = OpCode.fromInt(opCodeInt);
        instr /= 100;

        int numArgs = opCode.getNumArgs();

        ArrayList<Parameter> params = new ArrayList<>();
        // Read in the parameters for this opCode
        for (int i = 0; i < numArgs; i++) {
            ParamMode paramMode = ParamMode.fromInt(instr % 10);
            instr /= 10;
            Long paramValue = getValueAtPCAndAdvance();

            Parameter param = new Parameter(paramMode, paramValue);
            params.add(param);
        }

        Instruction nextInstruction = new Instruction(opCode, params);
        return nextInstruction;
    }

    public void addToInputQueue(Long inputValue) {
        this.inputQueue.add(inputValue);
    }

    public void addToInputQueue(String inputString) {
        // Make sure the input ends in a \n
        final String NEWLINE = System.getProperty("line.separator");
        if (!inputString.endsWith(NEWLINE)) inputString += NEWLINE;

        // Add the whole string to the input queue
        for (Character c : inputString.toCharArray()) {
            addToInputQueue((long) c);
        }
    }

    /*********** OpCode Implementations ***********/
    private boolean add(Instruction instruction) {
        // ADD
        // Adds together numbers read from two positions and
        // stores the result in a third position. The three integers
        // immediately after the opcode tell you these three positions
        // - the first two indicate the positions from which you should
        // read the input values, and the third indicates the position
        // at which the output should be stored.

        // Get the arguments
        Long val1 = getArgValue(instruction, 0);
        Long val2 = getArgValue(instruction, 1);
//        Long val3 = instruction.getParam(2).getValue();  // instructions that write out always use the value of the raw parameter
        Long val3 = getOutputArgValue(instruction, 2);  // instructions that write out always use the value of the raw parameter

        programCode.put(val3, val1 + val2);
        return true;
    }

    private boolean multiply(Instruction instruction) {
        // MULTIPLY
        // Works exactly like ADD, except it multiplies the
        // two inputs instead of adding them. Again, the three integers after
        // the opcode indicate where the inputs and outputs are, not their values.

        // Get the arguments
        Long val1 = getArgValue(instruction, 0);
        Long val2 = getArgValue(instruction, 1);
//        Long val3 = instruction.getParam(2).getValue();  // instructions that write out always use the value of the raw parameter
        Long val3 = getOutputArgValue(instruction, 2);  // instructions that write out always use the value of the raw parameter

        programCode.put(val3, val1 * val2);
        return true;
    }

    protected boolean input(Instruction instruction) {
        // INPUT
        // Opcode 3 takes a single integer as input and saves
        // it to the position given by its only parameter.
        // For example, the instruction 3,50 would take an
        // input value and store it at address 50.
        // Get the arguments
        Long val1 = getOutputArgValue(instruction, 0);
        Long inputValue;
        if ((inputValue = inputQueue.poll()) == null) {
            // If there NOT is an input waiting for us
            waitingForInput = true; // Set the flag that says we need input.
        } else {
            // If there IS an input waiting, process it.
            programCode.put(val1, inputValue);
            waitingForInput = false;    // and we're not waiting anymore
        }
        return true;
    }

    protected boolean output(Instruction instruction) {
        // OUTPUT
        // Opcode 4 outputs the value of its only parameter.
        // For example, the instruction 4,50 would output the
        // value at address 50.
        // Get the arguments
//        int val1 = instruction.getParam(0).getValue();  // instructions that write out always use the value of the raw parameter
        Long val1 = getArgValue(instruction, 0);
        lastOutput = val1;
//        System.out.println(val1);
        outputReady = true;
        return true;
    }

    private boolean jumpIfTrue(Instruction instruction) {
        // JUMP_IF_TRUE
        // jump-if-true: if the first parameter is *non-zero*,
        // it sets the instruction pointer to the value from the second parameter.
        // Otherwise, it does nothing.

        // Get the arguments
        Long val1 = getArgValue(instruction, 0);
//        int val2 = instruction.getParam(1).getValue();  // instructions that write out always use the value of the raw parameter
        Long val2 = getArgValue(instruction, 1);

        if (val1 != 0L) pc = val2;
        return true;
    }

    private boolean jumpIfFalse(Instruction instruction) {
        // JUMP_IF_FALSE
        // jump-if-true: if the first parameter is *zero*,
        // it sets the instruction pointer to the value from the second parameter.
        // Otherwise, it does nothing.

        // Get the arguments
        Long val1 = getArgValue(instruction, 0);
//        int val2 = instruction.getParam(1).getValue();  // instructions that write out always use the value of the raw parameter
        Long val2 = getArgValue(instruction, 1);

        if (val1 == 0L) pc = val2;
        return true;
    }

    private boolean lessThan(Instruction instruction) {
        // LESS_THAN
        // if the first parameter is less than the second parameter,
        // it stores 1 in the position given by the third parameter.
        // Otherwise, it stores 0.

        // Get the arguments
        Long val1 = getArgValue(instruction, 0);
        Long val2 = getArgValue(instruction, 1);
//        Long val3 = instruction.getParam(2).getValue();  // instructions that write out always use the value of the raw parameter
        Long val3 = getOutputArgValue(instruction, 2);  // instructions that write out always use the value of the raw parameter

        Long lessThan = (val1 < val2) ? 1L : 0L;
        programCode.put(val3, lessThan);
        return true;
    }

    private boolean equals(Instruction instruction) {
        // EQUALS
        // if the first parameter is equal to the second parameter,
        // it stores 1 in the position given by the third parameter.
        // Otherwise, it stores 0.

        // Get the arguments
        Long val1 = getArgValue(instruction, 0);
        Long val2 = getArgValue(instruction, 1);
//        Long val3 = instruction.getParam(2).getValue();  // instructions that write out always use the value of the raw parameter
        Long val3 = getOutputArgValue(instruction, 2);  // instructions that write out always use the value of the raw parameter

        Long equals = (val1.longValue() == val2.longValue()) ? 1L : 0L;
        programCode.put(val3, equals);
        return true;
    }

    private boolean relativeBase(Instruction instruction) {
        // RELATIVE_BASE
        // Opcode 9 adjusts the relative base by the value of its only parameter.
        // The relative base increases (or decreases, if the value is negative)
        // by the value of the parameter.

        Long val1 = getArgValue(instruction, 0);
        relativeBase += val1;
//        System.out.println("Relative Base:\t" + (relativeBase - val1) + "\t->\t" + val1 + "\t=\t" + relativeBase);
        return true;
    }

    private boolean halt(Instruction instruction) {
        // HALT
        // Stop execution by returning false.
        halted = true;
        return false;
    }

    protected Long getArgValue(Instruction instruction, int index) {
        ParamMode mode = instruction.getParam(index).getMode();
        Long arg = instruction.getParam(index).getValue();
        switch (mode) {
            case IMMEDIATE:
                return arg;
            case POSITION:
                return getValueAtPosition(arg);
            case RELATIVE:
                // The address a relative mode parameter refers to is itself plus the current relative base
                return getValueAtPosition(arg + relativeBase);
            default:
                return 0L;
        }
//        int value = (mode == IMMEDIATE) ? arg : getValueAtPosition(arg);
//        return value;
    }

    protected Long getOutputArgValue(Instruction instruction, int index) {
        ParamMode mode = instruction.getParam(index).getMode();
        Long arg = instruction.getParam(index).getValue();
        switch (mode) {
            case IMMEDIATE:
            case POSITION:
            default:
                return arg;
            case RELATIVE:
                return arg + relativeBase;
        }
    }


    public Long getLastOutput() {
        outputReady = false;    // Set the flag that we've consumed this output.
        return lastOutput;
    }

    public HashMap<Long, Long> getProgramCodeOriginal() {
        return programCodeOriginal;
    }

    public boolean isWaitingForInput() {
        return waitingForInput;
    }

    public boolean isOutputReady() {
        return outputReady;
    }

    public boolean isHalted() {
        return halted;
    }

    public void loadInputs(String inputFile) {
        // To load the program, simply read all the ints into an ArrayList.
        // We will interpret opcodes/arguments/pc as we execute it later
        HashMap<Long, Long> programCode = new HashMap<>();
        String line;
        try (BufferedReader br = new BufferedReader(new FileReader(inputFile))) {
            while ((line = br.readLine()) != null) {
                Long addr = 0L;
                String[] data = line.split(",");
                // Add all the codes from this line to the programCode list
                for (String element : data) {
                    programCode.put(addr, Long.parseLong(element));
                    addr++;
                }
            }
            // Initialize the CPU with the code we just loaded.
            this.programCodeOriginal = programCode;
            reset();
        } catch (Exception e) {
            System.out.println("Exception occurred: " + e.getMessage());
        }
        return;
    }
}
