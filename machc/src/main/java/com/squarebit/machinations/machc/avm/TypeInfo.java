package com.squarebit.machinations.machc.avm;

import com.squarebit.machinations.machc.ast.GGraph;
import com.squarebit.machinations.machc.avm.exceptions.FieldAlreadyExistedException;
import com.squarebit.machinations.machc.avm.exceptions.MachineException;
import com.squarebit.machinations.machc.avm.exceptions.MethodAlreadyExistedException;
import com.squarebit.machinations.machc.avm.runtime.TObject;
import com.squarebit.machinations.machc.avm.runtime.TObjectBase;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * A type declared in a {@link ModuleInfo}.
 */
public class TypeInfo {
    /**
     *
     */
    private static class FieldCache {
        private static final FieldCache INSTANCE = new FieldCache();

        private Field objectBaseTypeInfoField;
        private Field objectBaseFieldTableField;

        public FieldCache() {
            try {
                objectBaseTypeInfoField = TObjectBase.class.getDeclaredField("__typeInfo");
                objectBaseFieldTableField = TObjectBase.class.getDeclaredField("__fieldTable");

                objectBaseTypeInfoField.setAccessible(true);
                objectBaseFieldTableField.setAccessible(true);
            }
            catch (Exception ex) {
                throw new RuntimeException(ex);
            }
        }

        public static final Field TYPE_INFO_FIELD = INSTANCE.objectBaseTypeInfoField;
        public static final Field FIELD_TABLE_FIELD = INSTANCE.objectBaseFieldTableField;
    }

    private GGraph declaration;
    private ModuleInfo module;
    private String name;
    private Class implementingClass;

    //////////////////////
    // Fields
    private List<FieldInfo> fields;
    private Map<String, FieldInfo> fieldByName;

    /////////////////////
    // Methods
    private List<MethodInfo> methods;
    private Map<String, MethodInfo> methodByName;
    private Map<MethodSignature, MethodInfo> methodBySignature;

    ////////////////////
    // Constructors
    private MethodInfo internalInstanceConstructor;

    /**
     * Instantiates a new object.
     */
    public TypeInfo() {
        this.implementingClass = TObjectBase.class;

        this.fields = new ArrayList<>();
        this.fieldByName = new HashMap<>();

        this.methods = new ArrayList<>();
        this.methodByName = new HashMap<>();
        this.methodBySignature = new HashMap<>();

        // Internal instance constructor.
        this.internalInstanceConstructor = new MethodInfo()
                .setDeclaringType(this).setName("$__ctor__$")
                .setStatic(false).setConstructor(true);
    }

    /**
     * Gets declaration.
     *
     * @return the declaration
     */
    public GGraph getDeclaration() {
        return declaration;
    }

    /**
     * Sets declaration.
     *
     * @param declaration the declaration
     * @return the declaration
     */
    public TypeInfo setDeclaration(GGraph declaration) {
        this.declaration = declaration;
        return this;
    }

    /**
     * Gets the containing {@link ModuleInfo}.
     *
     * @return the containing module
     */
    public ModuleInfo getModule() {
        return module;
    }

    /**
     * Sets the containing {@link ModuleInfo}.
     *
     * @param module the containing module
     * @return this instance
     * @apiNote it is not recommended to use this method directly. Use {@link ModuleInfo#addType(TypeInfo)}
     * or {@link ModuleInfo#createType(String)} instead.
     *
     */
    public TypeInfo setModule(ModuleInfo module) {
        this.module = module;
        return this;
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
     * Sets the type name.
     *
     * @param name the type name
     * @return this instance
     * @apiNote it is not recommended to use this method directly. Use {@link ModuleInfo#addType(TypeInfo)}
     * or {@link ModuleInfo#createType(String)} instead.
     */
    public TypeInfo setName(String name) {
        this.name = name;
        return this;
    }

    /**
     * Gets implementing class.
     *
     * @return the implementing class
     */
    public Class getImplementingClass() {
        return implementingClass;
    }

    /**
     * Sets implementing class.
     *
     * @param implementingClass the implementing class
     * @return the implementing class
     */
    public TypeInfo setImplementingClass(Class implementingClass) {
        this.implementingClass = implementingClass;
        return this;
    }

    /**
     * Gets {@link FieldInfo} declared in this type.
     *
     * @return the field list in declaration order
     */
    public List<FieldInfo> getFields() {
        return fields;
    }

    /**
     * Create a new {@link FieldInfo} in this type.
     *
     * @param name the field name
     * @return the {@link FieldInfo} instance
     * @throws FieldAlreadyExistedException if a field with given name already existed.
     */
    public FieldInfo createField(String name) throws FieldAlreadyExistedException {
        FieldInfo fieldInfo = new FieldInfo().setDeclaringType(this).setName(name);
        addField(fieldInfo);
        return fieldInfo;
    }

    /**
     * Adds a {@link FieldInfo} to this type.
     *
     * @param fieldInfo the {@link FieldInfo} instance
     * @throws FieldAlreadyExistedException if a field with given name already existed.
     */
    public void addField(FieldInfo fieldInfo) throws FieldAlreadyExistedException {
        if (fieldByName.containsKey(fieldInfo.getName()))
            throw new FieldAlreadyExistedException(this, fieldInfo.getName());

        fieldInfo.setDeclaringType(this);
        fields.add(fieldInfo);
        fieldByName.put(fieldInfo.getName(), fieldInfo);
    }

    /**
     * Finds a field with given name.
     *
     * @param name the field name
     * @return the {@link FieldInfo} instance if found
     */
    public FieldInfo findField(String name) {
        return fieldByName.getOrDefault(name, null);
    }

    /**
     * Gets all methods declared in this type.
     *
     * @return the method list in declaration order
     */
    public List<MethodInfo> getMethods() {
        return methods;
    }

    /**
     * Creates a new {@link MethodInfo} in this type.
     *
     * @param name the method name
     * @return the {@link MethodInfo} instance
     * @throws MethodAlreadyExistedException the method already existed exception
     */
    public MethodInfo createMethod(String name) throws MethodAlreadyExistedException {
        MethodInfo methodInfo = new MethodInfo().setName(name);
        addMethod(methodInfo);
        return methodInfo;
    }

    /**
     * Adds a {@link MethodInfo} to this type.
     *
     * @param methodInfo the {@link MethodInfo} instance
     * @throws MethodAlreadyExistedException if a method with given name already existed.
     */
    public void addMethod(MethodInfo methodInfo) throws MethodAlreadyExistedException {
        if (methodByName.containsKey(methodInfo.getName()))
            throw new MethodAlreadyExistedException(this, methodInfo.getName());

        methodInfo.setDeclaringType(this);
        methods.add(methodInfo);
        methodByName.put(methodInfo.getName(), methodInfo);
    }

    /**
     * Finds a method with given name.
     * @param name the name.
     * @return
     */
    public MethodInfo findMethod(String name) {
        return methodByName.getOrDefault(name, null);
    }

    /**
     * Find a method with given name and parameter count.
     * @param name
     * @param parameterCount
     * @return
     */
    public MethodInfo findMethod(String name, int parameterCount) {
        return methodBySignature.getOrDefault(new MethodSignature(name, parameterCount), null);
    }

    /**
     * Gets internal instance constructor.
     *
     * @return the internal instance constructor.
     */
    public MethodInfo getInternalInstanceConstructor() {
        return internalInstanceConstructor;
    }

    /**
     * Allocate a new uninitialized instance of given type.
     * @return an instance of this type.
     * @throws MachineException if any errors occur.
     * @apiNote caller must be responsible for calling the type constructor.
     */
    public TObject allocateInstance() throws MachineException {
        try {
            TObject instance = (TObject)implementingClass.newInstance();

            if (instance instanceof TObjectBase) {
                TObjectBase objectBase = (TObjectBase)instance;

                setInstanceTypeInfo(objectBase);
                allocateInstanceFieldTable(objectBase);
            }

            return instance;
        }
        catch (Exception exception) {
            throw new MachineException(exception);
        }
    }

    /**
     * Allocate the field table for an instance of this type.
     */
    private void allocateInstanceFieldTable(TObjectBase instance) throws NoSuchFieldException, IllegalAccessException {
        Field nativeField = FieldCache.FIELD_TABLE_FIELD;
        int fieldCount = this.fields.size();
        nativeField.set(instance, new TObject[fieldCount]);
    }

    /**
     * Set instance type info.
     * @param instance the instance
     * @throws NoSuchFieldException
     * @throws IllegalAccessException
     */
    private void setInstanceTypeInfo(TObjectBase instance) throws NoSuchFieldException, IllegalAccessException {
        Field nativeField = FieldCache.TYPE_INFO_FIELD;
        nativeField.set(instance, this);
    }

    /**
     * Reindex fields.
     */
    public void reindexFields() {
        for (int i = 0; i < fields.size(); i++) {
            fields.get(i).setIndex(i);
        }
    }

    /**
     * Reindex methods.
     */
    public void reindexMethods() {
        methodBySignature.clear();
        methodByName.clear();

        for (int i = 0; i < methods.size(); i++) {
            MethodInfo methodInfo = methods.get(i);
            MethodSignature signature = new MethodSignature(methodInfo.getName(), methodInfo.getParameterCount());
            methodBySignature.put(signature, methodInfo);
            methodByName.put(methodInfo.getName(), methodInfo);
        }
    }
}
