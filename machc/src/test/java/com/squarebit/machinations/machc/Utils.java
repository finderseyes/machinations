package com.squarebit.machinations.machc;

import com.squarebit.machinations.machc.ast.GProgram;
import com.squarebit.machinations.machc.runtime.MachMachine;

import javax.crypto.Mac;
import java.io.File;
import java.net.URL;

public class Utils {
    public static String absoluteResourcePath(String path) throws Exception {
        ClassLoader classLoader = Utils.class.getClassLoader();
        URL resource = classLoader.getResource(path);

        if (resource == null)
            throw new Exception(String.format("Specified resource %s cannot be found.", path));

        File file = new File(resource.getFile());
        return file.getAbsolutePath();
    }

    public static GProgram compile(String path) throws Exception {
        String absolutePath = absoluteResourcePath(path);
        MachFrontend frontend = new MachFrontend();
        return frontend.compile(absolutePath);
    }

    public static MachMachine compileToMachine(String path) throws Exception {
        GProgram program = compile(path);
        return MachMachine.from(program);
    }
}
