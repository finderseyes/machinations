package com.squarebit.machinations.machc.runtime.components;

/**
 * A field of a type.
 */
public final class TField {
    private final String name;
    private final TType type;
    int fieldTableIndex = 0;


    /**
     * Instantiates a new field.
     *
     * @param name the field name
     * @param type the field type
     */
    protected TField(final String name, final TType type) {
        this.name = name;
        this.type = type;
    }

    /**
     * Gets field name.
     *
     * @return the field name
     */
    public String getName() {
        return name;
    }

    /**
     * Gets field type.
     *
     * @return the field type
     */
    public TType getType() {
        return type;
    }

    public void set(TObject obj, TObject value) {
        // obj.fieldTableEX.put(this, value);
        obj.fieldTable[fieldTableIndex] = value;
    }

    public TObject get(TObject obj) {
        return obj.fieldTable[fieldTableIndex];
    }
}
