package com.squarebit.machinations.machc.avm;

import com.squarebit.machinations.machc.avm.exceptions.MachineException;
import com.squarebit.machinations.machc.avm.runtime.TLambda;
import com.squarebit.machinations.machc.avm.runtime.TObject;

import java.lang.reflect.Field;

public class LambdaTypeInfo extends TypeInfo {
    /**
     *
     */
    private static class FieldCache {
        private static final FieldCache INSTANCE = new FieldCache();

        private Field typeInfoField;

        public FieldCache() {
            try {
                typeInfoField = TLambda.class.getDeclaredField("__typeInfo");
                typeInfoField.setAccessible(true);
            }
            catch (Exception ex) {
                throw new RuntimeException(ex);
            }
        }

        public static final Field TYPE_INFO_FIELD = INSTANCE.typeInfoField;
    }

    private MethodInfo lambdaMethod;
    private FieldInfo argumentsField;

    /**
     * Instantiates a new Lambda type info.
     */
    public LambdaTypeInfo() {
        this.setImplementingClass(TLambda.class);

        this.argumentsField = new FieldInfo().setType(ArrayTypeInfo.OBJECT_ARRAY).setName("arguments");
        try {
            this.addField(this.argumentsField);
        }
        catch (Exception exception) {
            throw new RuntimeException(exception);
        }
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
        if (implementingClass != TLambda.class)
            throw new RuntimeException("Unsupported operation");
        return super.setImplementingClass(implementingClass);
    }

    /**
     * Gets lambda method.
     *
     * @return the lambda method
     */
    public MethodInfo getLambdaMethod() {
        return lambdaMethod;
    }

    /**
     * Sets lambda method.
     *
     * @param lambdaMethod the lambda method
     * @return the lambda method
     */
    public LambdaTypeInfo setLambdaMethod(MethodInfo lambdaMethod) {
        this.lambdaMethod = lambdaMethod;
        return this;
    }

    /**
     * Gets argument field.
     *
     * @return the argument field
     */
    public FieldInfo getArgumentsField() {
        return argumentsField;
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
            TLambda instance = new TLambda();
            FieldCache.TYPE_INFO_FIELD.set(instance, this);

            return instance;
        }
        catch (Exception exception) {
            throw new MachineException(exception);
        }
    }
}
