package com.squarebit.machinations.machc.avm;

import com.squarebit.machinations.machc.avm.exceptions.MachineException;
import com.squarebit.machinations.machc.avm.runtime.TObject;
import com.squarebit.machinations.machc.avm.runtime.nodes.TGraphNode;

import java.lang.reflect.Field;

public class GraphNodeTypeInfo extends TypeInfo {
    /**
     *
     */
    private static class FieldCache {
        private static final FieldCache INSTANCE = new FieldCache();

        private Field typeInfoField;

        public FieldCache() {
            try {
                typeInfoField = TGraphNode.class.getDeclaredField("__typeInfo");
                typeInfoField.setAccessible(true);
            }
            catch (Exception ex) {
                throw new RuntimeException(ex);
            }
        }

        public static final Field TYPE_INFO_FIELD = INSTANCE.typeInfoField;
    }

    private TypeInfo graphType;

    public GraphNodeTypeInfo() {
        this.setImplementingClass(TGraphNode.class);
    }

    /**
     * Gets graph type.
     *
     * @return the graph type
     */
    public TypeInfo getGraphType() {
        return graphType;
    }

    /**
     * Sets graph type.
     *
     * @param graphType the graph type
     * @return the graph type
     */
    public GraphNodeTypeInfo setGraphType(TypeInfo graphType) {
        this.graphType = graphType;
        return this;
    }

    /**
     * Gets implementing class.
     *
     * @return the implementing class
     */
    @Override
    public Class getImplementingClass() {
        return TGraphNode.class;
    }

    /**
     * Sets implementing class.
     *
     * @param implementingClass the implementing class
     * @return the implementing class
     */
    @Override
    public TypeInfo setImplementingClass(Class implementingClass) {
        if (implementingClass != TGraphNode.class)
            throw new RuntimeException("Unsupported operation");
        return super.setImplementingClass(implementingClass);
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
            TGraphNode instance = new TGraphNode();
            FieldCache.TYPE_INFO_FIELD.set(instance, this);

            return instance;
        }
        catch (Exception exception) {
            throw new MachineException(exception);
        }
    }
}
