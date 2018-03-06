package com.squarebit.machinations.machc.runtime;

/**
 * Description of a type in the interpreter.
 */
public final class TType {
    private final String name;
    private final TType baseType;

    /**
     * Instantiates a new T type.
     *
     * @param name              the name
     * @param baseType          the base type
     */
    protected TType(final String name,
                    final TType baseType
                    )
    {
        this.name = name;
        this.baseType = baseType;
    }

    /**
     * Gets the type name.
     *
     * @return the type name
     */
    public String getName() {
        return name;
    }

    /**
     * Gets the type's base type.
     *
     * @return the base type
     */
    public TType getBaseType() {
        return baseType;
    }
}
