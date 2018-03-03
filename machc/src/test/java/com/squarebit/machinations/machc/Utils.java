package com.squarebit.machinations.machc;

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
}
