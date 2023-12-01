package org.jgoeres.adventofcode.common.intcode;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.HashMap;

public class IntCodeProcessorService {

    protected String inputFile;
    protected CPU cpu;

    public IntCodeProcessorService() {
    }

    public IntCodeProcessorService(String pathToFile) {
        inputFile = pathToFile;
        cpu = new CPU(pathToFile);
    }

    public void runToCompletion() {
        while (cpu.executeNext()) ;
    }

    public boolean executeNext() {
        return cpu.executeNext();
    }

    public void executeToNextInput() {
        while (!isWaitingForInput() &&
                !isHalted()) {
            executeNext();
        }
    }

    public void executeToNextOutput() {
        while (!isOutputReady()
                && !isHalted()) {
            executeNext();
            // If the processor is waiting for input, break out of this or it'll get stuck forever!
            if(isWaitingForInput()){
                break;}
        }
    }

    public void setValueAtPosition(Long position, Long value) {
        cpu.setValueAtPosition(position, value);
    }

    public void setCpuInputValue(Long cpuInputValue) {
        cpu.addToInputQueue(cpuInputValue);
    }

    public Long getValueAtPosition(Long position) {
        return cpu.getValueAtPosition(position);
    }

    public Long getProgramOutput() {
        return cpu.getLastOutput();
    }

    public boolean isWaitingForInput() {
        return cpu.isWaitingForInput();
    }

    public boolean isOutputReady() {
        return cpu.isOutputReady();
    }

    public boolean isHalted() {
        return cpu.isHalted();
    }

    protected String getInputFile() {
        return inputFile;
    }

    public void reset() {
        cpu.reset();
    }

    protected CPU loadInputs() {
        // TODO Refactor out calls to this from all days; use CPU's own loader instead
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
            return (new CPU(programCode));
        } catch (Exception e) {
            System.out.println("Exception occurred: " + e.getMessage());
        }
        return null;
    }
}
