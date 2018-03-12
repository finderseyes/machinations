package com.squarebit.machinations.machc.vm;

import com.squarebit.machinations.machc.vm.components.TType;

/**
 * Built in types.
 */
public final class Types {
    private static class Internal {
        private static UnitInfo CORE_UNIT = (UnitInfo)(new UnitInfo(null).setName("Core"));
        private static TypeInfo OBJECT_TYPE_INFO = createObjectTypeInfo();
        private static TypeInfo TYPE_TYPE_INFO = createTypeTypeInfo();

        private static TypeInfo createObjectTypeInfo() {
            TypeInfo typeInfo = new TypeInfo(CORE_UNIT);
            typeInfo.setName("Object");
            return typeInfo;
        }

        private static TypeInfo createTypeTypeInfo() {
            TypeInfo typeInfo = new TypeInfo(CORE_UNIT);
            typeInfo
                    .setBaseTypeInfo(OBJECT_TYPE_INFO)
                    .setName("Type");
            return typeInfo;
        }
    }

    public static TType OBJECT_TYPE = Internal.OBJECT_TYPE_INFO.getType();
    public static TType TYPE_TYPE = Internal.TYPE_TYPE_INFO.getType();
}
