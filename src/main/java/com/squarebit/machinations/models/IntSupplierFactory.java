package com.squarebit.machinations.models;

import java.util.function.Supplier;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class IntSupplierFactory {
    public Supplier<Integer> fromExpression(String expression) throws Exception {
        String pattern = "(((\\d*)D(\\d*))\\+?)?(-?\\d+)*";

        Pattern r = Pattern.compile(pattern);
        Matcher matcher = r.matcher(expression);

        if (matcher.find()) {
            String a = matcher.group(0);
            String b = matcher.group(1);
            String c = matcher.group(2);
            String d = matcher.group(3);
            String e = matcher.group(4);
            String f = matcher.group(5);
            int k = 10;

            return null;
        }
        else
            throw new Exception("Invalid expression.");
    }
}
