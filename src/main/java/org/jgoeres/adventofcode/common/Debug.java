package org.jgoeres.adventofcode.common;

public abstract class Debug {
    public static void debugPrint(boolean debug, String output) {
        if (debug) {
            System.out.println(output);
        }
    }
}
