package com.squarebit.machinations.machc.runtime.components;

import com.squarebit.machinations.machc.runtime.Frame;
import com.squarebit.machinations.machc.runtime.MachMachine;

/**
 * An object containing instructions to be executed on a machine.
 */
public abstract class TExecutable {
    MachMachine machine;
    Frame frame;

    public void invoke() {

    }
}
