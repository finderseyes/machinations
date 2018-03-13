package com.squarebit.machinations.machc.vm;

import com.squarebit.machinations.machc.Utils;
import org.junit.Test;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Future;

public class MachMachineTests {
    @Test
    public void specs_001() throws Exception {
        ProgramInfo program = Utils.compile("specs/specs-001.mach");
        TypeInfo typeInfo = program.findType("a").get(0);

        MachMachine machine = new MachMachine(program);
        machine.start();
        CompletableFuture<TObject> ref = machine.newInstance(typeInfo);

        ref.thenAccept(value -> {
            int x = 100;
        });

        TObject value = ref.get();
        machine.shutdown();
    }
}
