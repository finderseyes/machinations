package com.squarebit.machinations.machc;

import com.squarebit.machinations.machc.ast.GProgram;
import com.squarebit.machinations.machc.ast.GUnit;
import com.squarebit.machinations.machc.avm.Compiler;
import com.squarebit.machinations.machc.avm.ModuleInfo;
import com.squarebit.machinations.machc.avm.exceptions.CompilationException;
import com.squarebit.machinations.machc.vm.MachCompiler;
import com.squarebit.machinations.machc.vm.ProgramInfo;

import java.io.File;
import java.net.URL;
import java.util.Optional;

public class Utils {
    public static String absoluteResourcePath(String path) throws Exception {
        ClassLoader classLoader = Utils.class.getClassLoader();
        URL resource = classLoader.getResource(path);

        if (resource == null)
            throw new Exception(String.format("Specified resource %s cannot be found.", path));

        File file = new File(resource.getFile());
        return file.getAbsolutePath();
    }

    public static GProgram transform(String path) throws Exception {
        String absolutePath = absoluteResourcePath(path);
        MachFrontend frontend = new MachFrontend();
        return frontend.compile(absolutePath);
    }

    public static ProgramInfo compileold(String path) throws Exception {
        return MachCompiler.compile(transform(path));
    }

    public static ModuleInfo compile(String path) throws Exception {
        Compiler compiler = new Compiler();
        GProgram program = transform(path);

        Optional<GUnit> unit = program.getUnits().stream().findFirst();

        if (unit.isPresent())
            return compiler.compile(unit.get());
        else
            throw new Exception("Cannot find any unit in this program");
    }
}
