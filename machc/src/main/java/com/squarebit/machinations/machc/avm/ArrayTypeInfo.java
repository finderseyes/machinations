package com.squarebit.machinations.machc.avm;

import com.squarebit.machinations.machc.avm.exceptions.MachineException;
import com.squarebit.machinations.machc.avm.runtime.TArray;
import com.squarebit.machinations.machc.avm.runtime.TObject;

import java.lang.reflect.Field;

public class ArrayTypeInfo extends TypeInfo {

    public static final ArrayTypeInfo OBJECT_ARRAY = new ArrayTypeInfo().setElementType(CoreModule.OBJECT_TYPE);

    /**
     *
     */
    private static class FieldCache {
        private static final FieldCache INSTANCE = new FieldCache();

        private Field typeInfoField;

        public FieldCache() {
            try {
                typeInfoField = TArray.class.getDeclaredField("__typeInfo");
                typeInfoField.setAccessible(true);
            }
            catch (Exception ex) {
                throw new RuntimeException(ex);
            }
        }

        public static final Field TYPE_INFO_FIELD = INSTANCE.typeInfoField;
    }

    private TypeInfo elementType;

    public ArrayTypeInfo() {
        this.setImplementingClass(TArray.class);
    }

    /**
     * Gets implementing class.
     *
     * @return the implementing class
     */
    @Override
    public Class getImplementingClass() {
        return super.getImplementingClass();
    }

    /**
     * Sets implementing class.
     *
     * @param implementingClass the implementing class
     * @return the implementing class
     */
    @Override
    public TypeInfo setImplementingClass(Class implementingClass) {
        if (implementingClass != TArray.class)
            throw new RuntimeException("Unsupported operation");
        return super.setImplementingClass(implementingClass);
    }

    /**
     * Gets element type.
     *
     * @return the element type
     */
    public TypeInfo getElementType() {
        return elementType;
    }

    /**
     * Sets element type.
     *
     * @param elementType the element type
     * @return the element type
     */
    public ArrayTypeInfo setElementType(TypeInfo elementType) {
        this.elementType = elementType;
        return this;
    }

    /**
     * Allocate a new uninitialized instance of given type.
     *
     * @return an instance of this type.
     * @throws MachineException if any errors occur.
     * @apiNote caller must be responsible for calling the type constructor.
     */
    @Override
    public TObject allocateInstance() throws MachineException {
        try {
            TArray instance = new TArray();
            FieldCache.TYPE_INFO_FIELD.set(instance, this);

            return instance;
        }
        catch (Exception exception) {
            throw new MachineException(exception);
        }
    }
}
