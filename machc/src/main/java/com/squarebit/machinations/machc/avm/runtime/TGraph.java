package com.squarebit.machinations.machc.avm.runtime;

import com.squarebit.machinations.machc.avm.FieldInfo;
import com.squarebit.machinations.machc.avm.MethodInfo;
import com.squarebit.machinations.machc.avm.MethodRuntimeInfo;

import java.util.HashMap;
import java.util.Map;

/**
 * Base graph type.
 */
public class TGraph extends TObjectBase {
    private Map<MethodInfo, MethodRuntimeInfo> methodRuntimeInfos;
    private Map<String, FieldInfo> nodeFieldByName;
    private Map<String, FieldInfo> connectionFieldByName;

    /**
     * Instantiates a new T graph.
     */
    public TGraph() {
        this.methodRuntimeInfos = new HashMap<>();
        this.nodeFieldByName = new HashMap<>();
        this.connectionFieldByName = new HashMap<>();
    }

    /**
     * Called upon a field is set.
     *
     * @param fieldInfo
     * @param value
     */
    @Override
    protected void onSetField(FieldInfo fieldInfo, TObject value) {
        if (value instanceof TNode) {
            ((TNode)value).graph = this;

            nodeFieldByName.put(fieldInfo.getName(), fieldInfo);
        }
        else if (value instanceof TConnection) {
            ((TConnection)value).graph = this;
            connectionFieldByName.put(fieldInfo.getName(), fieldInfo);
        }
        super.onSetField(fieldInfo, value);
    }

    /**
     * Gets node.
     *
     * @param name the name
     * @return the node
     */
    public TNode getNode(String name) {
        FieldInfo fieldInfo = nodeFieldByName.getOrDefault(name, null);
        return (fieldInfo == null) ? null : (TNode)getField(fieldInfo);
    }

    /**
     * Gets connection.
     *
     * @param name the name
     * @return the connection
     */
    public TConnection getConnection(String name) {
        FieldInfo fieldInfo = connectionFieldByName.getOrDefault(name, null);
        return (fieldInfo == null) ? null : (TConnection)getField(fieldInfo);
    }

    /**
     *
     * @param methodInfo
     * @return
     */
    public MethodRuntimeInfo getMethodRuntimeInfo(MethodInfo methodInfo) {
        if (methodInfo.getDeclaringType() != this.getTypeInfo())
            throw new RuntimeException("Method does not exist");

        return methodRuntimeInfos.computeIfAbsent(methodInfo, m -> new MethodRuntimeInfo(methodInfo));
    }

    /**
     * Gets field.
     *
     * @param fieldName the field name
     * @return the field
     */
    public TObject getField(String fieldName) {
        FieldInfo fieldInfo = this.getTypeInfo().findField(fieldName);
        if (fieldInfo == null)
            throw new RuntimeException("Unknown field");

        return this.getField(fieldInfo);
    }

    /**
     * Sets field.
     *
     * @param fieldName the field name
     * @param value     the value
     */
    public void setField(String fieldName, TObject value) {
        FieldInfo fieldInfo = this.getTypeInfo().findField(fieldName);
        if (fieldInfo == null)
            throw new RuntimeException("Unknown field");
        this.setField(fieldInfo, value);
    }
}
