package com.squarebit.machinations;

import java.io.File;

public class Utils {
    public static String absoluteResourcePath(String path) {
        ClassLoader classLoader = Utils.class.getClassLoader();
        File file = new File(classLoader.getResource(path).getFile());
        return file.getAbsolutePath();
    }
}
