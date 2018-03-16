package com.squarebit.machinations.machc.avm;

import com.squarebit.machinations.machc.Utils;
import com.squarebit.machinations.machc.avm.runtime.TObject;
import com.squarebit.machinations.machc.vm.ProgramInfo;
import org.junit.Test;

public class MachineTests {
    @Test
    public void specs_001() throws Exception {
        ModuleInfo module = Utils.compile("specs/specs-001.mach");

        Machine machine = new Machine();
        machine.start();

        {
            TypeInfo typeInfo = module.findType("a");
            TObject graph = machine.newInstance(typeInfo).thenApply(v -> {
                int x = 10;
                return v;
            }).get();

            int k = 10;
        }

        machine.shutdown();
    }
}
