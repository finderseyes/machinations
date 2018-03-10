package com.squarebit.machinations.machc.runtime.components;

import com.squarebit.machinations.machc.MethodBase;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public final class TConstructor extends MethodBase {
    public static class Builder {
        private TConstructor target;
        private List<Consumer<TConstructor>> listeners = new ArrayList<>();

        public Builder(TType declaringType) {
            target = new TConstructor();
            target.declaringType = declaringType;
        }

        public Builder setName(String name) {
            target.name = name;
            return this;
        }

        public Builder addListener(Consumer<TConstructor> listener) {
            listeners.add(listener);
            return this;
        }

        public TConstructor build() {
            return target;
        }
    }

    private TType declaringType;
    private String name;
}
