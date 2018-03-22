package com.squarebit.machinations.machc.ast;

/**
 * The type G node type.
 */
public final class GNodeType {
    private boolean isBuiltin;
    private GNode.Type builtinType;
    private String typeName;

    public GNodeType(GNode.Type builtinType) {
        this.isBuiltin = true;
        this.builtinType = builtinType;
    }

    public GNodeType(String typeName) {
        this.isBuiltin = false;
        this.typeName = typeName;
    }

    public boolean isBuiltin() {
        return isBuiltin;
    }

    public GNode.Type getBuiltinType() {
        return builtinType;
    }

    public String getTypeName() {
        return typeName;
    }
}
