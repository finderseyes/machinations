package com.squarebit.machinations.machc.avm;

/**
 * A field declared in a {@link TypeInfo}.
 */
public final class FieldInfo {
    private TypeInfo declaringType;
    private TypeInfo type;
    private String name;

    /**
     * Instantiates a new object.
     */
    public FieldInfo() {
    }

    /**
     * Gets declaring {@link TypeInfo}.
     *
     * @return the declaring {@link TypeInfo}
     */
    public TypeInfo getDeclaringType() {
        return declaringType;
    }

    /**
     * Sets declaring {@link TypeInfo}.
     *
     * @param declaringType the declaring {@link TypeInfo}
     * @return this instance
     * @apiNote it is not recommended to use this method directly, use {@link TypeInfo#createField(String)} or
     * {@link TypeInfo#addField(FieldInfo)} instead.
     */
    public FieldInfo setDeclaringType(TypeInfo declaringType) {
        this.declaringType = declaringType;
        return this;
    }

    /**
     * Gets the field type.
     *
     * @return the field type
     */
    public TypeInfo getType() {
        return type;
    }

    /**
     * Sets the field type.
     *
     * @param type the field type
     * @return this instance
     */
    public FieldInfo setType(TypeInfo type) {
        this.type = type;
        return this;
    }

    /**
     * Gets the field name.
     *
     * @return the field name
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the field name.
     *
     * @param name the field name
     * @return this instance
     * @apiNote it is not recommended to use this method directly, use {@link TypeInfo#createField(String)} or
     * {@link TypeInfo#addField(FieldInfo)} instead.
     */
    public FieldInfo setName(String name) {
        this.name = name;
        return this;
    }
}
