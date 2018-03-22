package com.squarebit.machinations.machc.avm.runtime;

import com.squarebit.machinations.machc.avm.FieldInfo;

import java.util.HashMap;
import java.util.Map;

/**
 * Base graph type.
 */
public class TGraph extends TObjectBase {
    private Map<String, FieldInfo> nodeFieldByName;
    private Map<String, FieldInfo> connectionFieldByName;

    /**
     * Instantiates a new T graph.
     */
    public TGraph() {
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
}
