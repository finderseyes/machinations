package com.squarebit.machinations.machc;

import com.squarebit.machinations.machc.ast.GUnit;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class MachFrontendTests {
    @Test
    public void specs_001() throws Exception {
        MachFrontend frontend = new MachFrontend();
        GUnit unit = frontend.compileUnit(Utils.absoluteResourcePath("specs-001.mach"));
        assertThat(unit.findGraph("main")).isNotNull();
    }
}
