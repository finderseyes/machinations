package com.squarebit.machinations.models;

import org.apache.commons.lang3.RandomUtils;

import java.util.Random;
import java.util.function.Supplier;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class IntSupplierFactory {
    public Supplier<Integer> fromExpression(String expression) throws Exception {
        if (expression.trim().equals(""))
            return () -> 1;

        String pattern = "(((\\d*)D(\\d*))\\+?)?(-?\\d+)?";

        Pattern r = Pattern.compile(pattern);
        Matcher matcher = r.matcher(expression);

        if (matcher.find()) {
            String a = matcher.group(3);
            String b = matcher.group(4);
            String c = matcher.group(5);

            int aval = a != null ? (!a.equals("") ? Integer.parseInt(a) : 1) : 0;
            int bval = b != null && !b.equals("") ? Integer.parseInt(b) : 6;
            int cval = c != null && !c.equals("") ? Integer.parseInt(c) : 0;

            if (aval == 0)
                return () -> cval;
            else {
                return () -> {
                    int sum = 0;
                    for (int i = 0; i < aval; i++)
                        sum += RandomUtils.nextInt(1, bval + 1);
                    sum += cval;
                    return sum;
                };
            }
        }
        else
            throw new Exception("Invalid expression.");
    }
}
