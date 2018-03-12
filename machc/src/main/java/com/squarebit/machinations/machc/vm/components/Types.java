package com.squarebit.machinations.machc.vm.components;

import com.squarebit.machinations.machc.vm.TypeInfo;
import com.squarebit.machinations.machc.vm.UnitInfo;

/**
 * Built in types.
 */
public final class Types {
    public static class Internal {
        private static UnitInfo CORE_UNIT = (UnitInfo)(new UnitInfo(null).setName("Core"));

        public static TypeInfo OBJECT_TYPE_INFO = createObjectTypeInfo();
        public static TypeInfo VOID_TYPE_INFO = createVoidTypeInfo();
        public static TypeInfo TYPE_TYPE_INFO = createTypeTypeInfo();
        public static TypeInfo INTEGER_TYPE_INFO = createIntegerTypeInfo();
        public static TypeInfo GRAPH_TYPE_INFO = createGraphTypeInfo();

        private static TypeInfo createObjectTypeInfo() {
            TypeInfo typeInfo = new TypeInfo(CORE_UNIT);
            typeInfo.setImplementation(TObjectImpl.class)
                    .setName("Object");
            return typeInfo;
        }

        private static TypeInfo createVoidTypeInfo() {
            TypeInfo typeInfo = new TypeInfo(CORE_UNIT);
            typeInfo.setImplementation(TVoid.class)
                    .setName("Void");
            return typeInfo;
        }

        private static TypeInfo createTypeTypeInfo() {
            TypeInfo typeInfo = new TypeInfo(CORE_UNIT);
            typeInfo
                    .setBaseTypeInfo(OBJECT_TYPE_INFO)
                    .setImplementation(TType.class)
                    .setName("Type");
            return typeInfo;
        }

        private static TypeInfo createIntegerTypeInfo() {
            TypeInfo typeInfo = new TypeInfo(CORE_UNIT);
            typeInfo
                    .setBaseTypeInfo(OBJECT_TYPE_INFO)
                    .setImplementation(TInteger.class)
                    .setName("Integer");
            return typeInfo;
        }

        private static TypeInfo createGraphTypeInfo() {
            TypeInfo typeInfo = new TypeInfo(CORE_UNIT);
            typeInfo
                    .setBaseTypeInfo(OBJECT_TYPE_INFO)
                    .setImplementation(TGraph.class)
                    .setName("Graph");
            return typeInfo;
        }
    }

    public static TType OBJECT_TYPE = Internal.OBJECT_TYPE_INFO.getType();
    public static TType VOID_TYPE = Internal.VOID_TYPE_INFO.getType();
    public static TType TYPE_TYPE = Internal.TYPE_TYPE_INFO.getType();
    public static TType INTEGER_TYPE = Internal.INTEGER_TYPE_INFO.getType();
    public static TType GRAPH_TYPE = Internal.GRAPH_TYPE_INFO.getType();
}
