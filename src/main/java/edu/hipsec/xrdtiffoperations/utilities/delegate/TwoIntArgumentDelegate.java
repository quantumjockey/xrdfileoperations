package edu.hipsec.xrdtiffoperations.utilities.delegate;

// Induces me to wish C#-like delegate constructs existed in Java, but this will do for now

public interface TwoIntArgumentDelegate {
    void callMethod(int a, int b);
}
