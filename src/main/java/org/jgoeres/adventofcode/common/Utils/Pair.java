package org.jgoeres.adventofcode.common.Utils;

public class Pair<T>
{
    // Ref: https://stackoverflow.com/questions/28991757/using-generics-to-make-a-swap-method
    private T first;
    private T second;

    public Pair(T firstElement, T secondElement)
    {
        first = firstElement;
        second = secondElement;
    }

    public T getFirst() { return first; }

    public T getSecond() { return second; }

    public void swap()
    {

        T temp = first;
        first = second;
        second = temp;
    }

    public String toString() { return "(" + first + ", " + second + ")"; }
}
