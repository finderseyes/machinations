package com.squarebit.machinations.machc.avm;

import com.squarebit.machinations.machc.ast.GGraphField;
import com.squarebit.machinations.machc.avm.runtime.TObject;
import com.squarebit.machinations.machc.avm.runtime.TObjectBase;

/**
 * A field declared in a {@link TypeInfo}.
 */
public final class FieldInfo {
    private GGraphField declaration;
    private TypeInfo declaringType;
    private boolean isStatic;
    private TypeInfo type;
    private String name;
    private int index;

    /**
     * Instantiates a new object.
     */
    public FieldInfo() {
    }

    /**
     * Gets declaration.
     *
     * @return the declaration
     */
    public GGraphField getDeclaration() {
        return declaration;
    }

    /**
     * Sets declaration.
     *
     * @param declaration the declaration
     * @return the declaration
     */
    public FieldInfo setDeclaration(GGraphField declaration) {
        this.declaration = declaration;
        return this;
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
     * Determines if the field is static.
     *
     * @return true or false
     */
    public boolean isStatic() {
        return isStatic;
    }

    /**
     * Sets the field static status.
     *
     * @param isStatic static status
     * @return this instance
     */
    public FieldInfo setStatic(boolean isStatic) {
        this.isStatic = isStatic;
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

    /**
     * Gets index.
     *
     * @return the index
     */
    public int getIndex() {
        return index;
    }

    /**
     * Sets index.
     *
     * @param index the index
     * @return the index
     */
    public FieldInfo setIndex(int index) {
        this.index = index;
        return this;
    }

    /**
     * Gets the field value of an instance.
     *
     * @param instance the instance
     * @return the field value
     */
    public TObject get(TObject instance) {
        if (instance instanceof TObjectBase) {
            return ((TObjectBase)instance).getField(index);
        }
        throw new RuntimeException("The given object does not have specified field");
    }

    /**
     * Sets the field value.
     *
     * @param instance the instance
     * @param value    the value
     */
    public void set(TObject instance, TObject value) {
        if (instance instanceof TObjectBase) {
            ((TObjectBase)instance).setField(index, value);
        }
        throw new RuntimeException("The given object does not have specified field");
    }
}
