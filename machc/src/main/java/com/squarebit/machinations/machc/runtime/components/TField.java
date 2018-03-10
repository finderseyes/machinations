package com.squarebit.machinations.machc.runtime.components;

import com.squarebit.machinations.machc.ast.GGraphField;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

/**
 * A field of a type.
 */
public final class TField {
    public static class Builder {
        private TField target;
        private List<Consumer<TField>> listeners = new ArrayList<>();

        public Builder(TType declaringType) {
            this.target = new TField();
            target.declaringType = declaringType;
        }

        public Builder setDeclaration(GGraphField declaration) {
            target.declaration = declaration;
            return this;
        }

        public Builder setName(String name) {
            target.name = name;
            return this;
        }

        public Builder setType(TType type) {
            target.type = type;
            return this;
        }

        public Builder addListener(Consumer<TField> listener) {
            this.listeners.add(listener);
            return this;
        }

        public TField build() {
            listeners.forEach(l -> l.accept(target));
            return target;
        }
    }

    private GGraphField declaration;
    private TType declaringType;
    private String name;
    private TType type;

    /**
     *
     */
    private TField(){

    }

    public GGraphField getDeclaration() {
        return declaration;
    }

    public TType getDeclaringType() {
        return declaringType;
    }

    public String getName() {
        return name;
    }

    public TType getType() {
        return type;
    }

    public void set(TObject obj, TObject value) {
        // obj.fieldTableEX.put(this, value);
        // obj.fieldTable[fieldTableIndex] = value;
    }

    public TObject get(TObject obj) {
        return null;
        // return obj.fieldTable[fieldTableIndex];
    }
}
