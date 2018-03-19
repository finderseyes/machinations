package com.squarebit.machinations.machc.avm.runtime;

import com.squarebit.machinations.machc.avm.CoreModule;
import com.squarebit.machinations.machc.avm.TypeInfo;

public class TNamedResource implements TObject, TSetElement {
    private final String typeName;

    /**
     * Instantiates a new T named resource.
     */
    public TNamedResource() {
        this.typeName = "";
    }

    /**
     * Instantiates a new T named resource.
     *
     * @param typeName the type name
     */
    public TNamedResource(String typeName) {
        this.typeName = typeName;
    }

    /**
     * Gets information of the type of this class.
     *
     * @return the {@link TypeInfo}
     */
    @Override
    public TypeInfo getTypeInfo() {
        return CoreModule.NAMED_RESOURCE;
    }

    /**
     * Name of the element name.
     *
     * @return name of the element type
     */
    @Override
    public String getTypeName() {
        return this.typeName;
    }
}
