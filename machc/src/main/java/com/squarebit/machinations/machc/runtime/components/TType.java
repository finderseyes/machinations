package com.squarebit.machinations.machc.runtime.components;

/**
 * Description of a type in the interpreter.
 */
public final class TType<T extends TObject> {
    private final String name;
    private final TType baseType;
    private final Class<T> implementation;
    private final boolean valueType;
    private final TField[] fields;
    private final TMethod[] methods;

    /**
     * Instantiates a new type.
     *
     * @param name           the name
     * @param baseType       the base type
     * @param implementation the implementation
     * @param valueType      the value type
     * @param fields         the fields
     * @param methods        the methods
     */
    protected TType(final String name,
                    final TType baseType,
                    final Class<T> implementation,
                    boolean valueType,
                    final TField[] fields,
                    final TMethod[] methods
                    )
    {
        this.name = name;
        this.baseType = baseType;
        this.implementation = implementation;
        this.valueType = valueType;
        this.fields = fields;
        this.methods = methods;

        for (int i = 0; i < fields.length; i++)
            fields[i].fieldTableIndex = i;
    }

    /**
     * Instantiates a new type.
     *
     * @param name           the name
     * @param baseType       the base type
     * @param implementation the implementation
     */
    public TType(final String name,
                    final TType baseType,
                    final Class<T> implementation)
    {
        this(name, baseType, implementation, false, new TField[0], new TMethod[0]);
    }

    public TType(final TType baseType, final Class<T> implementation) {
        this(implementation.getSimpleName(), baseType, implementation);
    }

    /**
     * Instantiates a new T type.
     *
     * @param baseType       the base type
     * @param implementation the implementation
     * @param valueType      the value type
     */
    public TType(final TType baseType, final Class<T> implementation, boolean valueType) {
        this(implementation.getSimpleName(), baseType, implementation, valueType, new TField[0], new TMethod[0]);
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

    /**
     * Determines if the type is value type.
     *
     * @return the value type.
     */
    public boolean isValueType() {
        return valueType;
    }

    /**
     * Creates a new instance of the type.
     *
     * @return an instance of this type
     * @throws InstantiationException the instantiation exception
     * @throws IllegalAccessException the illegal access exception
     */
    public T newInstance() throws InstantiationException, IllegalAccessException {
        T instance = implementation.newInstance();

        instance.type = this;
        instance.fieldTable = buildInstanceFieldTable();

        for (TField field: fields) {
            if (field.getType().isValueType()) {
                TObject value = field.getType().newInstance();
                field.set(instance, value);
            }
        }

        return instance;
    }

    /**
     * Gets implementation class.
     *
     * @return the implementation class.
     */
    protected Class<T> getImplementation() {
        return implementation;
    }

    /**
     * Builds the field table for this type.
     * @return the field table.
     */
    private TObject[] buildInstanceFieldTable() {
        return new TObject[fields.length];
    }
}
