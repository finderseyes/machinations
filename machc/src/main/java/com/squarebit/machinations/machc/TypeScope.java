package com.squarebit.machinations.machc;

import com.squarebit.machinations.machc.runtime.components.TField;
import com.squarebit.machinations.machc.runtime.components.TMethod;
import com.squarebit.machinations.machc.runtime.components.TType;

import java.util.HashMap;
import java.util.Map;

public class TypeScope extends Scope {
    private TType type;
    private Map<String, TField> fieldByName = new HashMap<>();
    private Map<String, TMethod> methodByName = new HashMap<>();

    public TypeScope(Scope parent, TType type) {
        super(parent);
        this.type = type;

        for (TField field : type.getFields())
            fieldByName.put(field.getName(), field);

        for (TMethod method: type.getMethods())
            methodByName.put(method.getName(), method);
    }

    public TType getType() {
        return type;
    }

    @Override
    protected Object findLocalSymbol(String name) {
        Object result = fieldByName.getOrDefault(name, null);

        if (result == null)
            result = methodByName.getOrDefault(name, null);

        return result;
    }
}
